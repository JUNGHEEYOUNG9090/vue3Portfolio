<template>
	<div>
		<h2 class="pa-5">개발로그 등록</h2>
		<v-divider :thickness="2"></v-divider>
	</div>
	<v-container fluid>
		<v-row class="align-center">
			<v-col cols="auto">
				<v-icon class="mr-2">mdi-pencil</v-icon>
				<label for="title" icon="mdi-pen">글제목</label>
			</v-col>
			<v-col>
				<input
					v-model="title"
					type="text"
					id="title"
					class="outlined-input"
					placeholder="제목을 입력하세요"
				/>
			</v-col>
		</v-row>
	</v-container>
	<v-container>
		<v-row>
			<v-col>
				<div
					ref="editorContainer"
					@drop.prevent="handleDrop"
					@dragover.prevent
				></div>
				<v-row>
					<v-col>
						<v-sheet class="d-flex mb-6">
							<v-sheet class="ma-2 pa-2 me-auto"></v-sheet>
							<v-sheet class="ma-1 pa-1"
								><v-btn color="primary" @click="gobackListPage"
									>취소</v-btn
								></v-sheet
							>
							<v-sheet class="ma-1 pa-1"
								><v-btn color="success" @click="saveData" :loading="isLoading"
									>저장</v-btn
								></v-sheet
							>
						</v-sheet>
					</v-col>
				</v-row>
			</v-col>
		</v-row>
	</v-container>
	<!-- 크기 조정 팝업 -->
	<v-dialog v-model="dialog" max-width="400">
		<v-card>
			<v-card-title>이미지 크기 조정</v-card-title>
			<v-card-text>
				<v-row>
					<v-col>
						<v-text-field
							v-model="imageWidth"
							label="너비(px)"
							type="number"
						></v-text-field>
					</v-col>
					<v-col>
						<v-text-field
							v-model="imageHeight"
							label="높이(px)"
							type="number"
						></v-text-field>
					</v-col>
				</v-row>
			</v-card-text>
			<v-card-actions>
				<v-btn text @click="dialog = false">취소</v-btn>
				<v-btn text @click="resizeImage">확인</v-btn>
			</v-card-actions>
		</v-card>
	</v-dialog>
</template>

<script setup>
import { onMounted, onUnmounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import Quill from 'quill';
import 'quill/dist/quill.snow.css';
import axios from 'axios';

let quillEditor;
const router = useRouter();
const title = ref('');
const editorContainer = ref(null);
const today = new Date();
const isLoading = ref(false);
const dialog = ref(false);
const imageWidth = ref(0);
const imageHeight = ref(0);
let selectedImage = null;

const formatDateToYYYYMMDD = date => {
	const year = date.getFullYear();
	const month = String(date.getMonth() + 1).padStart(2, '0');
	const day = String(date.getDate()).padStart(2, '0');

	return `${year}${month}${day}`;
};

const formattedDate = formatDateToYYYYMMDD(today);

// 게시글 작성 함수
const saveData = async () => {
	isLoading.value = true; // 저장이 완료될때까지 대기
	try {
		const coverImageElement = quillEditor.root.querySelector('img');
		let coverImageUrl = null;

		// 커버 이미지 URL로 변환
		if (coverImageElement) {
			const coverImageBase64 = coverImageElement.src;
			const coverImageFile = await fetch(coverImageBase64).then(res =>
				res.blob(),
			);
			const formData = new FormData();
			formData.append('file', coverImageFile, `coverImage_${Date.now()}.jpg`);

			const coverResponse = await axios.post(
				'http://localhost:8080/uploadDevImage',
				formData,
			);
			coverImageUrl = coverResponse.data; // 서버에서 받은 이미지 URL
		}

		const contentWithUrls = await convertImagesInContent(
			quillEditor.root.innerHTML,
		);
		const response = await axios.post('http://localhost:8080/saveDevLog', {
			title: title.value,
			content: contentWithUrls,
			create_dt: formattedDate,
			create_user: 'admin',
			update_dt: formattedDate,
			update_user: 'admin',
			coverImage: coverImageUrl,
		});

		console.log('저장 완료', response.data);
		alert('저장되었습니다');
		gobackListPage();
	} catch (error) {
		console.error('post에러: ', error);
	} finally {
		isLoading.value = false;
	}
};

// 모든 이미지를 URL로 변환하는 함수
const convertImagesInContent = async content => {
	const doc = new DOMParser().parseFromString(content, 'text/html');
	const images = doc.querySelectorAll('img');

	for (const img of images) {
		const base64 = img.src;

		// Base64 문자열인지 확인
		if (base64.startsWith('data:image/')) {
			// Base64 문자열에서 'data:image/jpeg;base64,' 부분을 제거
			const base64Data = base64.split(',')[1];
			const byteCharacters = atob(base64Data);
			const byteNumbers = new Array(byteCharacters.length);
			for (let i = 0; i < byteCharacters.length; i++) {
				byteNumbers[i] = byteCharacters.charCodeAt(i);
			}
			const byteArray = new Uint8Array(byteNumbers);
			const file = new Blob([byteArray], { type: 'image/jpeg' }); // MIME 타입 설정

			const formData = new FormData();
			formData.append('file', file, `image_${Date.now()}.jpg`); // 적절한 파일명 사용

			try {
				// 이미지 업로드 후 URL 받기
				const response = await axios.post(
					'http://localhost:8080/uploadDevImage',
					formData,
				);
				const imageUrl = response.data; // 서버에서 받은 이미지 URL

				// img.src를 URL로 변경
				img.src = imageUrl;
			} catch (error) {
				console.error('이미지 업로드 실패:', error);
			}
		}
	}

	return doc.body.innerHTML; // URL로 변경된 HTML 반환
};

// 이미지 드롭 처리 함수
const handleDrop = async event => {
	const files = event.dataTransfer.files;
	if (files.length) {
		const file = files[0];
		if (!file.type.startsWith('image/')) {
			alert('이미지 파일만 드롭할 수 있습니다.');
			return;
		}

		const formData = new FormData();
		formData.append('file', file);

		try {
			const response = await axios.post(
				'http://localhost:8080/uploadDevImage',
				formData,
			);
			const imageUrl = response.data;
			const range = quillEditor.getSelection();
			// 이미지가 이미 삽입된 경우 확인
			const imgElements = editorContainer.value.querySelectorAll('img');
			const isImageAlreadyInserted = Array.from(imgElements).some(
				img => img.src === imageUrl,
			);

			if (!isImageAlreadyInserted && range) {
				quillEditor.insertEmbed(range.index, 'image', imageUrl); // 이미지 삽입

				quillEditor.setSelection(range.index + 1); // 커서 이동
			}
		} catch (error) {
			console.error('에러메세지:', error.message);
		}
	}
};

const resizeImage = () => {
	if (selectedImage) {
		selectedImage.style.width = `${imageWidth.value}px`;
		selectedImage.style.height = `${imageHeight.value}px`;
	}
	dialog.value = false;
};

const handleImageClick = event => {
	if (event.target.tagName === 'IMG') {
		selectedImage = event.target;
		imageWidth.value = selectedImage.clientWidth;
		imageHeight.value = selectedImage.clientHeight;
		dialog.value = true; // 팝업 열기
	}
};

const gobackListPage = () => {
	router.push({
		name: 'DevLog',
	});
};

onMounted(() => {
	quillEditor = new Quill(editorContainer.value, {
		theme: 'snow',
		placeholder: '내용을 입력하세요',
		toolbar: [
			['bold', 'italic', 'underline'],
			[{ list: 'ordered' }, { list: 'bullet' }],
			['clean'],
		],
	});

	// 이미지 클릭 이벤트 핸들러
	editorContainer.value.addEventListener('click', handleImageClick);
});

onUnmounted(() => {
	editorContainer.value.removeEventListener('click', handleImageClick);
});
</script>

<style scoped>
.outlined-input {
	width: 100%;
	border: 1px solid;
	border-radius: 4px;
	padding: 8px;
	outline: none;
	transition: border-color 0.3s;
}
</style>
