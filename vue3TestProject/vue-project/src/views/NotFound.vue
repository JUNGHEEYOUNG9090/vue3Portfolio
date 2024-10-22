<template>
	<v-container>
		<div class="text-h3 mb-1">404 Not Found</div>
		<v-divider></v-divider>
		<div class="text-h6">요청한 페이지를 찾을 수 없습니다</div>
		<RouterLink to="/">
			<div class="d-flex justify-center align-center" style="height: 50vh">
				<v-btn color="primary" height="100" width="200" style="font-size: 25px">
					HOME
				</v-btn>
			</div>
		</RouterLink>
	</v-container>
</template>

<script setup>
import { RouterLink } from 'vue-router';
</script>

<style scoped>
.primary-button {
	background-color: #1976d2 !important;
	color: white !important;
	border: 1px solid #1976d2 !important;
	margin-top: auto;
}
</style>
