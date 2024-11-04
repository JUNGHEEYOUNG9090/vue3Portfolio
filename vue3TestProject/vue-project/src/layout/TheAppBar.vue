<template>
	<v-app id="inspire">
		<v-app-bar class="px-3" density="compact" flat color="success">
			<v-spacer></v-spacer>
			<v-tabs color="#263238" centered>
				<v-tab
					v-for="link in links"
					:key="link.id"
					:text="link.menuname"
					:value="link"
					:to="link.to"
				></v-tab>
			</v-tabs>
			<v-spacer></v-spacer>
		</v-app-bar>

		<v-main class="bg-grey-lighten-3">
			<v-container>
				<v-row>
					<v-col cols="12" md="2">
						<v-sheet
							min-height="300"
							rounded="lg"
							max-width="160"
							style="
								background-color: beige;
								display: flex;
								align-items: center;
								justify-content: center;
							"
						>
							<MyPhoto></MyPhoto> </v-sheet
					></v-col>

					<v-col cols="12" md="8">
						<v-sheet min-height="70vh" rounded="lg">
							<TheView></TheView>
						</v-sheet>
					</v-col>
					<v-col cols="12" md="2"> </v-col>
				</v-row>
			</v-container>
		</v-main>
	</v-app>
</template>

<script setup>
import TheView from '@/layout/TheView.vue';
import MyPhoto from '@/components/MyPhoto.vue';
const links = [
	{ id: 1, menuname: 'Home', to: '/' },
	{ id: 2, menuname: 'Image', to: '/imagelist' },
	{ id: 3, menuname: 'Career', to: '/Career' },
	{ id: 4, menuname: 'DevLog', to: '/DevelopLogList' },
];
</script>

<style scoped></style>
