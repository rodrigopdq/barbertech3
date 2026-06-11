import axios from 'axios';

export const api = axios.create({
  baseURL: 'http://localhost:8080',
  headers: {
    'Content-Type': 'application/json',
  }
});

// O interceptor agora busca o token que o login guardou no navegador
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('barbertech_token');
  
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
}, (error) => {
  return Promise.reject(error);
});