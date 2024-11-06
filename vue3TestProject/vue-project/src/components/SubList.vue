<template>
	<div>
		<v-card class="mx-auto my-4">
			<v-list v-model:opened="open">
				<v-list-item
					prepend-icon="mdi-tools"
					title="포트폴리오 제작에 사용된 기술들"
				></v-list-item>
				<v-divider :thickness="2"></v-divider>
				<v-list-group value="Vue3.js">
					<template v-slot:activator="{ props }">
						<v-list-item v-bind="props" title="Vue3.js"></v-list-item>
					</template>

					<v-list-group value="Vue3.js">
						<template v-slot:activator="{ props }">
							<v-icon>mdi-arrow-right-bold</v-icon
							><span>
								본 포트폴리오의 UI를 위해 사용하게 된 기술입니다. 컴포넌트 별
								재사용이 가능하고 vuetify를 통해 더 직관적으로 만들 수 있기
								때문에 사용하게 되었습니다.
							</span>
						</template>
					</v-list-group>
				</v-list-group>

				<v-list-group value="Eclipse">
					<template v-slot:activator="{ props }">
						<v-list-item v-bind="props" title="Eclipse"></v-list-item>
					</template>

					<v-list-group value="Eclipse">
						<template v-slot:activator="{ props }">
							<v-icon>mdi-arrow-right-bold</v-icon
							><span>
								본 포트폴리오의 서버 및 백단을 위해 사용하게 된 기술입니다.
								Spring을 통해 기본적인 설정과 적용시킬 기술들을 선택하였습니다.
								RESTful API를 통해 Vue3와 연결하였고 Java코드를 이용하여 백단을
								작성하였습니다. boot dashboard를 통해 서버를 구동시켰습니다.
							</span>
						</template>
					</v-list-group>
				</v-list-group>

				<v-list-group value="MariaDB">
					<template v-slot:activator="{ props }">
						<v-list-item v-bind="props" title="MariaDB"></v-list-item>
					</template>

					<v-list-group value="MariaDB">
						<template v-slot:activator="{ props }">
							<v-icon>mdi-arrow-right-bold</v-icon
							><span>
								본 포트폴리오의 데이터베이스를 담당하기 위해 사용하게 된
								기술입니다. MySQL의 쿼리언어가 유사하고 무료로 사용할 수
								있습니다. DBeaver라는 툴을 통하여 데이터 관리를 하였습니다.
							</span>
						</template>
					</v-list-group>
				</v-list-group>

				<v-list-group value="GIT">
					<template v-slot:activator="{ props }">
						<v-list-item v-bind="props" title="GIT"></v-list-item>
					</template>

					<v-list-group value="GIT">
						<template v-slot:activator="{ props }">
							<v-icon>mdi-arrow-right-bold</v-icon
							><span>
								본 포트폴리오의 형상관리 및 GitHub에 업로드시키기 위해 사용
								하였습니다.
							</span>
						</template>
					</v-list-group>
				</v-list-group>
			</v-list>
		</v-card>
	</div>
</template>

<script setup>
import { ref } from 'vue';

const open = ref(['Users']);
</script>

<style scoped></style>
