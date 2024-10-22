<template>
	<div>
		<v-card class="d-flex justify-center" variant="outlined"
			><v-img
				max-width="100%"
				max-height="100%"
				aspect-ratio="16/9"
				cover
				:src="myPhoto"
				alt="이미지를 찾을 수 없습니다"
			></v-img>
		</v-card>
		<v-card variant="tonal" rounded="0">
			<v-card-text class="pt-4" variant="tonal">
				포트폴리오 만들기
			</v-card-text>
		</v-card>
	</div>
</template>

<script setup>
import myPhoto from '/images/myPhoto.jpg';
</script>

<style scoped></style>
