<template>
	<div>
		<h2 class="pa-5">경력</h2>
		<v-divider :thickness="2"></v-divider>
		<v-container>
			<v-row>
				<v-col cols="2">
					<div class="py-4">
						<h5 class="ma-2">연도선택</h5>
						<v-menu>
							<template v-slot:activator="{ props }">
								<v-btn v-bind="props">{{ selectedTitle || '연도' }}</v-btn>
							</template>

							<v-list>
								<v-list-item
									v-for="item in items"
									:key="item.id"
									@click="selectItem(item.id)"
								>
									<v-list-item-title>{{ item.year }}</v-list-item-title>
								</v-list-item>
							</v-list>
						</v-menu>
					</div>
				</v-col>

				<v-divider vertical></v-divider>
				<v-col cols="8">
					<div v-if="selectedItem">
						<h3>{{ selectedItem.id }}년</h3>
						<v-list>
							<v-list-item
								v-for="career in selectedItem.careers"
								:key="career.id"
							>
								<GridComponent
									v-if="career"
									class="pre-line"
									:company="career.company"
									:period="career.period"
									:work="career.work"
									:skill="career.skill"
									:text="career.text"
								/>
							</v-list-item>
						</v-list>
					</div>
				</v-col>
			</v-row>
		</v-container>
	</div>
</template>

<script setup>
import { ref } from 'vue';
import { careerData } from '@/career/careerData/CareerData';
import GridComponent from '@/components/GridComponent.vue';

const items = ref([
	{ id: '2024', year: '2024' },
	{ id: '2023', year: '2023' },
	{ id: '2022', year: '2022' },
	{ id: '2021', year: '2021' },
	{ id: '2020', year: '2020' },
]); // 년도 데이터

const selectedTitle = ref('');
const selectedItem = ref(null);

// ID로 경력 데이터를 가져오기
const fetchCareerById = id => {
	const data = careerData.find(item => item.id === id);
	selectedItem.value = data;
};

const selectItem = id => {
	const selectedYear = items.value.find(item => item.id === id).year;
	selectedTitle.value = selectedYear;
	fetchCareerById(id); // 선택한 ID로 데이터 조회
};
</script>

<style scoped>
.background {
	background-image: url('images/carrerBack.jpg'); /* 배경 이미지 설정 */
	background-size: 100%;
	background-repeat: repeat;
	max-width: 100%; /* 그리드의 너비에 맞춤 */
	overflow: hidden; /* 이미지가 넘치지 않도록 설정 */
}
.pre-line {
	white-space: pre-wrap;
}
</style>
