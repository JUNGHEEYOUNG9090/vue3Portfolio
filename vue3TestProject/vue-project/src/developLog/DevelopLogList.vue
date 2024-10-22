<template>
	<div>
		<h2 class="pa-5">개발로그</h2>
		<v-divider :thickness="2"></v-divider>
		<v-container>
			<v-row justify="end">
				<v-col cols="auto">
					<v-btn
						color="primary"
						densisty="compact"
						icon="mdi-plus"
						height="24"
						width="24"
						to="/DevelopLogCreate"
					></v-btn>
				</v-col>
			</v-row>
			<v-row>
				<!-- 개발일지 목록 -->
				<v-col
					cols="12"
					v-for="devLog in devLogs"
					:key="devLog.id"
					@click="goDetailPage(devLog.id)"
				>
					<DevCard :devLog="devLog"></DevCard>
				</v-col>
			</v-row>
		</v-container>
	</div>
</template>

<script setup>
import axios from 'axios';
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import DevCard from '@/components/DevCard.vue';

const devLogs = ref([]);
const router = useRouter();

const fetchDevLogs = async () => {
	try {
		const response = await axios.get('http://localhost:8080/devLoglist');
		console.log('Fetched devLogs:', response.data); // 전체 응답 확인
		devLogs.value = response.data.map(log => {
			// coverImage 경로를 HTTP URL로 변환
			const coverImage = log.coverImage
				? `http://localhost:8080/images/devlog/${log.coverImage.split('/').pop()}`
				: null;
			return { ...log, coverImage };
		});
	} catch (error) {
		console.error('Error fetching devLogs:', error);
	}
};

const goDetailPage = id => {
	router.push({
		name: 'DevDetail',
		params: { id },
	});
};

onMounted(() => {
	fetchDevLogs();
});
</script>

<style scoped></style>
