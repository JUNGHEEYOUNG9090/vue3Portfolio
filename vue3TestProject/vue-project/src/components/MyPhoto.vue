<template>
	<div>
		<v-card
			class="d-flex justify-center"
			elevation="0"
			style="background-color: transparent; padding-top: 10%"
		>
			<v-img
				class="ma-1"
				max-width="100%"
				max-height="100%"
				cover
				:src="myPhoto"
				style="border-radius: 50%; overflow: hidden"
				alt="이미지를 찾을 수 없습니다"
			></v-img>
		</v-card>
		<v-card
			class="pt-4"
			elevation="0"
			rounded="0"
			style="background-color: beige; height: 30%; width: 100%"
		>
		</v-card>
		<v-card-text
			class="pt-4 text-center"
			style="background-color: white; color: black; width: 100%"
		>
			정희영의 <br />포트폴리오입니다
		</v-card-text>
		<v-card
			elevation="0"
			rounded="0"
			style="
				background-color: beige;
				height: 10px;
				width: 100%;
				text-align: center;
			"
		></v-card>
	</div>
</template>

<script setup>
import myPhoto from '/images/myPhoto.jpg';
</script>

<style scoped></style>
