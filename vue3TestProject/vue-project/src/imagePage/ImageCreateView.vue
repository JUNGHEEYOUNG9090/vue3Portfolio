<template>
	<div>
		<h2 class="pa-5">이미지 생성</h2>
	</div>
	<v-divider :thickness="2"></v-divider>
	<div>
		<v-container>
			<v-file-input
				v-model="selectedFile"
				label="이미지를 넣어주세요"
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
					<v-card-text class="pre-line">
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
			><v-btn color="primary" @click="gobackListPage">취소</v-btn></v-sheet
		>
		<v-sheet class="ma-1 pa-1"
			><v-btn color="success" @click="saveData">저장</v-btn></v-sheet
		>
	</v-sheet>
	<div>
		<v-dialog v-model="dialog" max-width="400" persistent>
			<v-card color="success">
				<v-card-title>
					<span class="headline">저장되었습니다</span>
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
</template>

<script setup>
import { ref } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';

const router = useRouter();
const selectedFile = ref(null); // 선택된 파일을 저장할 변수
const uploadedImageUrl = ref(null); // 미리보기 이미지를 저장할 변수
const cardText = ref('');
const dialog = ref(false);
const today = new Date();

const formatDateToYYYYMMDD = date => {
	const year = date.getFullYear();
	const month = String(date.getMonth() + 1).padStart(2, '0');
	const day = String(date.getDate()).padStart(2, '0');

	return `${year}${month}${day}`;
};

const formattedDate = formatDateToYYYYMMDD(today);

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
		console.error('잘못된 파일');
	}
};

const saveData = async () => {
	let file = selectedFile.value;

	if (Array.isArray(file)) {
		file = file[0];
	}

	if (!file || !cardText.value) {
		console.error('이미지파일을 선택해주세요.');
		return;
	}

	const formData = new FormData();
	formData.append('file', file);
	formData.append('text', cardText.value);
	formData.append('path', 'D:/uploads');
	formData.append('createDate', formattedDate);
	formData.append('updateDate', formattedDate);
	formData.append('createUser', 'admin');
	formData.append('updateUser', 'admin');

	try {
		const response = await axios.post(
			'http://54.180.213.168:8080/saveImageCard',
			formData,
		);
		console.log('저장 성공:', response.data);
		showAlert();
	} catch (error) {
		console.error('에러메세지:', error.message);
	}
};

const showAlert = () => {
	dialog.value = true;
};

const gobackListPage = () => {
	router.push({
		name: 'ImageListView',
	});
};
</script>

<style scoped>
.pre-line {
	white-space: pre-wrap;
}
.primary-button {
	background-color: #1976d2 !important;
	color: white !important;
	border: 1px solid #1976d2 !important;
}
</style>
