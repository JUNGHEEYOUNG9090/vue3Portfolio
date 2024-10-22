import { createRouter, createWebHistory } from 'vue-router';

import TheHome from '@/views/TheHome.vue';
import TestComponent from '@/components/TestComponent.vue';
import ImageListView from '@/imagePage/ImageListView.vue';
import ImageCreateView from '@/imagePage/ImageCreateView.vue';
import ImageDetailView from '@/imagePage/ImageDetailView.vue';
import NotFound from '@/views/NotFound.vue';
import CareerView from '@/career/CareerView.vue';
import DevelopLogList from '@/developLog/DevelopLogList.vue';
import DevelopLogCreate from '@/developLog/DevelopLogCreate.vue';
import DevelopLogDetail from '@/developLog/DevelopLogDetail.vue';
import DevelopLogEdit from '@/developLog/DevelopLogEdit.vue';

const routes = [
	{
		path: '/',
		name: 'Home',
		component: TheHome,
	},
	{
		path: '/imagelist',
		name: 'ImageListView',
		component: ImageListView,
	},
	{
		path: '/imageDetail/:id',
		name: 'ImageDetailView',
		component: ImageDetailView,
	},
	{
		path: '/test',
		name: 'TestComponent',
		component: TestComponent,
	},
	{
		path: '/imageCreate',
		name: 'ImageCreateView',
		component: ImageCreateView,
	},
	{
		path: '/career',
		name: 'Career',
		component: CareerView,
	},
	{
		path: '/DevelopLogList',
		name: 'DevLog',
		component: DevelopLogList,
	},
	{
		path: '/DevelopLogCreate',
		name: 'DevCreate',
		component: DevelopLogCreate,
	},
	{
		path: '/DeveopLogDetail/:id',
		name: 'DevDetail',
		component: DevelopLogDetail,
	},
	{
		path: '/DevelopLogEdit/:id',
		name: 'DevEdit',
		component: DevelopLogEdit,
	},
	{
		path: '/:pathMatch(.*)*',
		name: 'NotFound',
		component: NotFound,
	},
];

const router = createRouter({
	history: createWebHistory('/'),
	routes,
});
export default router;
