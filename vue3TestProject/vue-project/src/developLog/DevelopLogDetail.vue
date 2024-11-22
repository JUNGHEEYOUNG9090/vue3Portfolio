<template>
	<div>
		<h2 class="pa-5">개발로그 ({{ title }})</h2>
		<v-divider :thickness="2"></v-divider>
	</div>
	<v-container fluid>
		<v-row class="align-center">
			<v-col cols="auto">
				<v-icon class="mr-2">mdi-book-open-page-variant</v-icon>
			</v-col>
		</v-row>
	</v-container>
	<v-container>
		<v-row>
			<v-col>
				<div v-html="content"></div>
			</v-col>
		</v-row>
	</v-container>
	<v-container>
		<v-row>
			<v-col>
				<v-row>
					<v-col>
						<v-sheet class="d-flex mb-6">
							<v-sheet class="ma-2 pa-2 me-auto"></v-sheet>
							<v-sheet class="ma-1 pa-1"
								><v-btn color="primary" @click="gobackListPage"
									>목록</v-btn
								></v-sheet
							>
							<v-sheet class="ma-1 pa-1"
								><v-btn color="error" @click="deleteData">삭제</v-btn></v-sheet
							>
							<v-sheet class="ma-1 pa-1"
								><v-btn color="warning" @click="goEditData"
									>수정</v-btn
								></v-sheet
							>
						</v-sheet>
					</v-col>
				</v-row>
			</v-col>
		</v-row>
	</v-container>
</template>

<script setup>
import axios from 'axios';
import { onMounted, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

const router = useRouter();
const route = useRoute();
const title = ref('');
const content = ref('');
const imageUrls = ref([]);

const fetchDevLogDetail = async id => {
	try {
		const response = await axios.get(
			`http://54.180.213.168:8080/devlogDetail/${id}`,
		);
		const devLog = response.data;

		title.value = devLog.title;
		content.value = devLog.content; // 내용 설정

		// content에서 이미지 URL을 추출하여 imageUrls 배열에 추가
		const tempDiv = document.createElement('div');
		tempDiv.innerHTML = devLog.content;
		const imgElements = tempDiv.querySelectorAll('img');

		imgElements.forEach(img => {
			imageUrls.value.push(img.src); // src를 imageUrls 배열에 추가
		});
	} catch (error) {
		console.error('데이터 로드 에러:', error);
	}
};

const gobackListPage = () => {
	router.push({
		name: 'DevLog',
	});
};

const goEditData = () => {
	router.push({ name: 'DevEdit', params: { id: route.params.id } });
};

const deleteData = () => {
	const id = route.params.id;
	console.log('id:', id);
	try {
		const response = axios.delete(
			`http://54.180.213.168:8080/deleteDevlog/${id}`,
		);
		console.log(response.data); // 서버에서 반환된 메시지 출력
		alert('삭제되었습니다.');
		gobackListPage();
	} catch (error) {
		console.error('이미지 삭제 중 오류 발생:', error);
		alert('삭제에 실패했습니다.');
	}
};

onMounted(() => {
	fetchDevLogDetail(route.params.id);
});
</script>

<style>
/* 이미지 크기 조정 */
img {
	max-width: 100%; /* 부모 컨테이너의 가로폭에 맞춤 */
	height: 100%; /* 이미지 비율 유지 */
	width: 100%; /* 이미지가 가로 100%를 차지하도록 강제 */
}

.outlined-input {
	width: 100%;
	border: 1px solid;
	border-radius: 4px;
	padding: 8px;
	outline: none;
	transition: border-color 0.3s;
}
</style>
