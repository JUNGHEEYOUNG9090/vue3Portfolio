<template>
	<div>
		<h2 class="pa-5">이미지 게시판</h2>
	</div>
	<v-divider :thickness="2"></v-divider>
	<div>
		<v-container fluid>
			<v-row justify="end">
				<v-col cols="auto">
					<v-btn
						color="primary"
						densisty="compact"
						icon="mdi-plus"
						height="24"
						width="24"
						to="/imageCreate"
					></v-btn>
				</v-col>
			</v-row>
			<v-row>
				<v-col v-for="image in images" :key="image.id" cols="3">
					<image-card
						:imageId="image.image_id"
						:imageSrc="`http://localhost:8080/files/${image.image_name}`"
						:text="image.image_text"
						@click="goDetailPage(image.image_id)"
					></image-card>
				</v-col>
			</v-row>
		</v-container>
	</div>
</template>

<script setup>
import ImageCard from '@/components/ImageCard.vue';
import axios from 'axios';

import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();
const images = ref([]);

// API 호출 함수
const fetchData = async () => {
	try {
		const response = await axios.get('http://localhost:8080/api/images');
		images.value = response.data;
	} catch (error) {
		console.error('Error fetching users:', error);
	}
};

const goDetailPage = id => {
	router.push({
		name: 'ImageDetailView',
		params: { id },
	});
};

// 컴포넌트가 마운트되면 데이터를 자동으로 가져옴
onMounted(fetchData);
</script>
<style lang="scss" scoped></style>
