<template>
	<div class="grid-background">
		<v-container>
			<div>
				<v-container class="background">
					<v-row>
						<h2 class="pa-4" style="color: #ff9800">{{ company }}</h2>
					</v-row>
					<v-row>
						<v-col cols="2">
							<v-sheet class="pa-1 ma-1 grid-background">
								<strong style="color: white">기간</strong>
							</v-sheet>
						</v-col>
						<v-col cols="10">
							<v-sheet class="pa-1 ma-1 grid-background"
								><strong style="color: white">{{ period }}</strong></v-sheet
							>
						</v-col>
					</v-row>

					<v-row no-gutters>
						<v-col cols="2">
							<v-sheet class="pa-1 ma-1 grid-background">
								<strong style="color: white">업무</strong>
							</v-sheet>
						</v-col>
						<v-col cols="10">
							<v-sheet class="pa-1 ma-1 grid-background"
								><strong style="color: white">{{ work }}</strong></v-sheet
							>
						</v-col>
					</v-row>

					<v-row no-gutters>
						<v-col cols="2">
							<v-sheet class="pa-1 ma-1 grid-background">
								<strong style="color: white">기술</strong>
							</v-sheet>
						</v-col>
						<v-col cols="10">
							<v-sheet class="pa-1 ma-1 grid-background"
								><strong style="color: white">{{ skill }}</strong></v-sheet
							>
						</v-col>
					</v-row>

					<v-row no-gutters>
						<v-col cols="2">
							<v-sheet class="pa-1 ma-1 grid-background">
								<strong style="color: white">코멘트</strong>
							</v-sheet>
						</v-col>
						<v-col cols="10">
							<v-sheet class="pa-1 ma-1 grid-background"
								><strong style="color: white">{{ text }}</strong></v-sheet
							>
						</v-col>
					</v-row>
				</v-container>
			</div>
		</v-container>
	</div>
</template>
<script setup>
import { defineProps } from 'vue';
/* eslint-disable no-unused-vars */
const props = defineProps({
	company: {
		type: String,
	},
	period: {
		type: String,
	},
	work: {
		type: String,
	},
	skill: {
		type: String,
	},
	text: {
		type: String,
	},
});
</script>

<style scoped>
.grid-background {
	background-color: transparent; /* 배경을 투명하게 설정 */
}
</style>
