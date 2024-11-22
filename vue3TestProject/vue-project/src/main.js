import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import vuetify from './plugins/vuetify';
import { loadFonts } from './plugins/webfontloader';
import axios from 'axios';

loadFonts();
axios.defaults.baseURL = 'http://54.180.213.168:8080';

const app = createApp(App);
app.config.globalProperties.$axios = axios;
app.use(router);
app.use(vuetify);
app.mount('#app');
