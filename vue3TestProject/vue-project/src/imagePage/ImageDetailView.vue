<template>
	<div>
		<h2 class="pa-5">이미지 수정</h2>
	</div>
	<v-divider :thickness="2"></v-divider>
	<div>
		<v-container>
			<v-file-input
				v-model="selectedFile"
				:label="
					uploadedFileName
						? `현재 파일: ${uploadedFileName}`
						: '이미지파일을 넣어주세요'
				"
				prepend-icon="mdi-upload"
				accept="image/*"
				@change="previewImage"
			></v-file-input>
		</v-container>
	</div>
	<v-row>
		<v-col>
			<div></div>
		</v-col>
	</v-row>
	<v-row>
		<v-col>
			<v-container>
				<h5 class="text-center mb-3">미리보기</h5>
				<!-- 미리보기 제목을 카드 위에 배치 -->
				<v-card class="mx-auto" max-width="344">
					<div class="d-flex justify-center pa-3">
						<v-img
							v-if="uploadedImageUrl"
							:src="uploadedImageUrl"
							max-width="300"
							max-height="300"
							alt="Uploaded Image"
						></v-img>
					</div>
					<v-card-text class="pre-wrap">
						{{ cardText }}
					</v-card-text>
				</v-card>
			</v-container>
		</v-col>
		<v-col>
			<v-container>
				<v-textarea
					v-model="cardText"
					label="이미지카드에 넣을 텍스트"
				></v-textarea>
			</v-container>
		</v-col>
	</v-row>
	<v-sheet class="d-flex mb-6">
		<v-sheet class="ma-2 pa-2 me-auto"></v-sheet>
		<v-sheet class="ma-1 pa-1"
			><v-btn color="primary" @click="gobackListPage">목록</v-btn></v-sheet
		>
		<v-sheet class="ma-1 pa-1"
			><v-btn color="error" @click="deleteData">삭제</v-btn></v-sheet
		>
		<v-sheet class="ma-1 pa-1"
			><v-btn color="success" @click="updateData">저장</v-btn></v-sheet
		>
	</v-sheet>
	<div>
		<v-dialog v-model="dialogSave" max-width="400" persistent>
			<v-card color="success">
				<v-card-title>
					<span class="headline">저장</span>
				</v-card-title>
				<v-card-text> 이미지가 성공적으로 저장되었습니다. </v-card-text>
				<v-card-actions>
					<v-spacer></v-spacer>
					<v-btn class="primary-button" elevated @click="gobackListPage"
						>확인</v-btn
					>
				</v-card-actions>
			</v-card>
		</v-dialog>
	</div>
	<div>
		<v-dialog v-model="dialogDelete" max-width="400" persistent>
			<v-card color="success">
				<v-card-title>
					<span class="headline">삭제</span>
				</v-card-title>
				<v-card-text> 이미지가 성공적으로 삭제되었습니다. </v-card-text>
				<v-card-actions>
					<v-spacer></v-spacer>
					<v-btn class="primary-button" elevated @click="gobackListPage"
						>확인</v-btn
					>
				</v-card-actions>
			</v-card>
		</v-dialog>
	</div>
</template>

<script setup>
import axios from 'axios';
import { onMounted, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

const router = useRouter();
const route = useRoute();
const cardText = ref('');
const selectedFile = ref(null);
const uploadedFileName = ref('');
const dialogSave = ref(false);
const dialogDelete = ref(false);
const uploadedImageUrl = ref(null); // 미리보기 이미지를 저장할 변수
const today = new Date();
const imageId = route.params.image_id; // 라우트에서 id 가져오기

const formatDateToYYYYMMDD = date => {
	const year = date.getFullYear();
	const month = String(date.getMonth() + 1).padStart(2, '0');
	const day = String(date.getDate()).padStart(2, '0');

	return `${year}${month}${day}`;
};
const formattedDate = formatDateToYYYYMMDD(today);

const fetchImageData = async () => {
	try {
		const response = await axios.get(
			`http://54.180.213.168:8080/images/${imageId}`, // URL 수정
		);
		uploadedImageUrl.value = `http://54.180.213.168:8080/files/${response.data.image_name}`;
		cardText.value = response.data.image_text; // 응답 데이터의 프로퍼티 이름 확인
		uploadedFileName.value = response.data.image_name;
	} catch (error) {
		console.error('Error fetching image:', error);
	}
};

// 파일이 변경될 때 미리보기 이미지 URL 생성
const previewImage = () => {
	let file = selectedFile.value;

	if (Array.isArray(file)) {
		file = file[0];
	}

	if (file && file instanceof File) {
		console.log('선택된 파일:', file); // 파일 확인 로그
		uploadedImageUrl.value = URL.createObjectURL(file); // 파일을 URL로 변환하여 미리보기
	} else {
		console.error('Invalid file input');
	}
};

const updateData = async () => {
	let file = selectedFile.value;

	if (Array.isArray(file)) {
		file = file[0];
	}

	const formData = new FormData();
	formData.append('file', file);
	formData.append('text', cardText.value);
	formData.append('path', 'D:/uploads');
	formData.append('updateDate', formattedDate);
	formData.append('updateUser', 'admin');

	try {
		const response = await axios.put(
			`http://54.180.213.168:8080/updateImageCard/${imageId}`,
			formData,
		);
		console.log('저장 성공:', response.data);
		showSaveAlert();
	} catch (error) {
		console.error('Error message:', error.message);
	}
};

const showSaveAlert = () => {
	dialogSave.value = true;
};

const showDeleteAlert = () => {
	dialogDelete.value = true;
};

const deleteData = async id => {
	try {
		const response = await axios.delete(
			`http://54.180.213.168:8080/deleteImageCard/${imageId}`,
		);
		console.log(response.data); // 서버에서 반환된 메시지 출력
		showDeleteAlert();
	} catch (error) {
		console.error('이미지 삭제 중 오류 발생:', error);
		alert('이미지 삭제에 실패했습니다.');
	}
};

const gobackListPage = () => {
	router.push({ name: 'ImageListView' });
};

// 컴포넌트가 마운트될 때 데이터 불러오기
onMounted(fetchImageData);
</script>

<style lang="scss" scoped></style>
