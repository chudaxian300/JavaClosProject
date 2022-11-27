import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import Antd from 'ant-design-vue';
import 'ant-design-vue/dist/antd.css';
import * as Icons from '@ant-design/icons-vue';
import axios from 'axios';
import { Tool } from './util/tool';

axios.defaults.baseURL = process.env.VUE_APP_SERVER

axios.interceptors.request.use((request: any) => {
    console.log('请求参数: ', request);
    const token = store.state.user.token
    if (Tool.isNotEmpty(token)) {
        request.headers.token = token;
        console.log("请求headers增加token: ", token)
    }
    return request;
}, error => {
    return Promise.reject(error);
});

axios.interceptors.response.use((response) => {
    console.log('返回结果: ', response);
    return response;
}, error => {
    return Promise.reject(error);
});

const app = createApp(App);
app.use(store).use(router).use(Antd).mount('#app');

const icons: any = Icons;
for (const i in icons) {
    app.component(i, icons[i])
}

console.log('环境', process.env.NODE_ENV)
