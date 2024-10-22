<template>
	<v-card class="mx-auto pa-3" max-width="344" hover>
		<v-img :width="300" aspect-ratio="16/9" cover :src="imageSrc"></v-img>

		<v-card-text>
			{{ text }}
		</v-card-text>
	</v-card>
</template>

<script setup>
import { defineProps } from 'vue';
/* eslint-disable no-unused-vars */
const props = defineProps({
	imageId: {
		type: Number,
		required: true,
	},
	imageSrc: {
		type: String,
		required: true,
	},
	text: {
		type: String,
	},
});
</script>

<style lang="scss" scoped></style>
