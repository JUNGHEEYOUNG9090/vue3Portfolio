<template>
	<div>
		<h2 class="pa-5">Home</h2>
		<v-divider :thickness="2"></v-divider>
	</div>
	<v-container>
		<v-row>
			<v-col cols="4">
				<v-card class="mx-auto my-4" max-width="400" elevation="2">
					<v-card-title class="d-flex align-center">
						<svg-icon type="mdi" :path="path"></svg-icon>

						개인정보</v-card-title
					>
					<v-card-text>
						<v-row>
							<v-col cols="3">이름 </v-col>
							<v-divider vertical></v-divider>
							<v-col>정희영</v-col>
						</v-row>
						<v-divider></v-divider>
						<v-row>
							<v-col cols="3">생년월일</v-col>
							<v-divider vertical></v-divider>
							<v-col>1990년 9월 26일</v-col>
						</v-row>
						<v-divider></v-divider>
						<v-row>
							<v-col cols="3">주소 </v-col>
							<v-divider vertical></v-divider>
							<v-col>서울시 중랑구</v-col>
						</v-row>
						<v-divider></v-divider>
						<v-row>
							<v-col cols="3">GitHub </v-col>
							<v-divider vertical></v-divider>
							<v-col
								><a href="https://github.com/JUNGHEEYOUNG9090/vue3Portfolio.git"
									>https://github.com/JUNGHEEYOUNG9090/vue3Portfolio.git</a
								></v-col
							>
						</v-row>
					</v-card-text>
				</v-card>
			</v-col>
			<v-col>
				<sub-list></sub-list>
			</v-col>
		</v-row>
		<v-row>
			<v-col cols="4">
				<v-list-item-title class="d-flex align-center">
					<svg-icon type="mdi" :path="path2"></svg-icon>
					최근 게시물</v-list-item-title
				>
				<v-list class="background" elevation="2">
					<v-list-item
						v-for="post in posts"
						:key="post.update_dt"
						@click="goDetailPage(post.id, post.type)"
					>
						<v-list-item-title>{{ post.title }}</v-list-item-title>
						<v-list-item-subtitle>
							<span>메뉴: {{ post.type }}</span> |
							<span>수정일: {{ post.update_dt }}</span>
						</v-list-item-subtitle>
					</v-list-item>

					<!-- Pagination Buttons -->
					<div style="display: flex; justify-content: center; margin-top: 10px">
						<v-icon @click="prevPage" :disabled="page === 0">
							mdi-arrow-left-bold</v-icon
						>
						<span style="margin: 0 10px"
							>Page {{ page + 1 }} of {{ totalPages }}</span
						>
						<v-icon @click="nextPage" :disabled="page === totalPages - 1">
							mdi-arrow-right-bold</v-icon
						>
					</div>
				</v-list>
			</v-col>
			<v-col
				><div class="background-container" style="height: 50vh"></div
			></v-col>
		</v-row>
	</v-container>
</template>

<script setup>
import axios from 'axios';
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import SvgIcon from '@jamescoyle/vue-icon';
import { mdiAccount, mdiNewBox } from '@mdi/js';
import SubList from '@/components/SubList.vue';

const router = useRouter();
const posts = ref([]); //전체 게시물
const page = ref(0);
const size = ref(5);
const totalPages = ref(1);
const path = mdiAccount;
const path2 = mdiNewBox;

const fetchPosts = async () => {
	try {
		const response = await axios.get('http://54.180.213.168:8080/posts', {
			params: {
				page: page.value,
				size: size.value,
			},
		});
		posts.value = response.data.content;
		totalPages.value = response.data.totalPages;
	} catch {
		console.error('페이지를 불러오는데 실패했습니다');
	}
};

const nextPage = () => {
	if (page.value < totalPages.value - 1) {
		page.value++;
		fetchPosts();
	}
};
const prevPage = () => {
	if (page.value > 0) {
		page.value--;
		fetchPosts();
	}
};

const goDetailPage = (id, type) => {
	if (type === 'devLog') {
		router.push({
			name: 'DevDetail',
			params: { id },
		});
	} else {
		router.push({
			name: 'ImageDetailView',
			params: { id },
		});
	}
};

onMounted(fetchPosts);
</script>

<style scoped>
.background {
	border: 1px solid;
}
.background-container {
	background-image: url('/images/pikachu.png'); /* public 폴더 안에 이미지가 있을 때 */
	background-size: 40vh;
	background-position: right;
}
</style>
