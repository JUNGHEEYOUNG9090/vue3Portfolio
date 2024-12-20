import { fileURLToPath, URL } from 'node:url';

import { defineConfig } from 'vite';
import vue from '@vitejs/plugin-vue';

// https://github.com/vuetifyjs/vuetify-loader/tree/next/packages/vite-plugin
import vuetify from 'vite-plugin-vuetify';

// https://vitejs.dev/config/
export default defineConfig({
	plugins: [vue(), vuetify({ autoImport: true })],
	server: {
		port: 3000,
		proxy: {
			'/api': {
				target: 'http://54.180.213.168:8080',
				changeOrigin: true,
				rewrite: path => path.replace(/^\/api/, ''),
			},
			'/images': 'http://54.180.213.168:8080',
		},
	},
	build: {
		outDir: '../src/main/resources/static',
		assetsDir: './',
	},
	resolve: {
		alias: {
			'@': fileURLToPath(new URL('./src', import.meta.url)),
		},
	},
});
