<template>
	<div>
		<v-card
			class="mx-auto"
			height="200px"
			max-width="800"
			width="auto"
			hover
			:style="{ border: '1px solid #ccc' }"
		>
			<template v-slot:title>
				<div class="title-container">
					<span class="font-weight-black">{{ devLog.title }}</span>
					<div class="update-info">
						<div>수정일: {{ formattedUpdateDate }}</div>
						<!-- null일 경우 'N/A' 표시 -->
						<div>수정: {{ devLog.update_user }}</div>
						<!-- null일 경우 'N/A' 표시 -->
					</div>
				</div>
			</template>
			<v-divider :thickness="4" color="black"></v-divider>

			<v-row>
				<v-col cols="9">
					<v-card-text class="bg-surface-light pt-4 pre-line">
						{{ truncatedContent }}
					</v-card-text>
				</v-col>
				<v-col cols="3" class="pa-4">
					<v-img
						:src="
							devLog.coverImage
								? devLog.coverImage
								: require('images/not_found_image.jpg')
						"
						:alt="devLog.title"
						max-height="100px"
						width="auto"
					></v-img>
				</v-col>
			</v-row>
		</v-card>
	</div>
</template>

<script setup>
import { computed } from 'vue';

/* eslint-disable no-unused-vars */
const props = defineProps({
	devLog: {
		type: Object,
		required: true,
	},
});
// 수정일을 포맷하는 계산된 속성
const formattedUpdateDate = computed(() => {
	const dateString = props.devLog.update_dt;

	if (typeof dateString !== 'string' || dateString.length !== 8) {
		return ''; // update_dt가 올바른 형식이 아닐 경우 빈 문자열 반환
	}

	// YYYYMMDD 형식으로 가정하고 분리
	const year = dateString.substring(0, 4);
	const month = dateString.substring(4, 6);
	const day = dateString.substring(6, 8);

	return `${year}.${month}.${day}`;
});

// content에서 <로 시작하고 />로 끝나는 문자열을 제거한 후 100자로 잘라서 반환하는 계산된 속성
const truncatedContent = computed(() => {
	let content = props.devLog.content;
	if (typeof content !== 'string') {
		return ''; // 문자열이 아닌 경우 공백
	}

	// <br> 태그를 줄 바꿈 문자로 대체
	content = content.replace(/<br\s*\/?>/gi, '\n');

	// 모든 HTML 태그 제거
	content = content.replace(/<\/?[^>]+(>|$)/g, ''); // 정규 표현식으로 HTML 태그 제거

	// 길이가 100자보다 크면 첫 150자만 반환하고, 그 뒤에 ...을 붙임
	return content.length > 150 ? content.substring(0, 150) + '...' : content;
});
</script>

<style scoped>
.title-container {
	display: flex;
	justify-content: space-between; /* 제목과 수정 정보를 양쪽 끝으로 분리 */
	align-items: center; /* 세로 정렬을 중앙으로 */
	line-height: 1.2; /* 줄 간격을 줄이기 위한 설정 */
}

.update-info {
	text-align: right; /* 수정 정보를 오른쪽 정렬 */
	margin-top: 4px; /* 제목과 수정 정보 간의 간격 */
	font-size: 0.5em; /* 수정 정보의 폰트 크기 조정 (선택 사항) */
	color: #666; /* 수정 정보의 색상 (선택 사항) */
}

.pre-line {
	white-space: pre-wrap;
}
</style>
