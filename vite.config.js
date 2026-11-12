import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  base: '/',
  plugins: [react()],
  server: {
    // o frontend chama /api/... e o Vite repassa ao backend Spring Boot
    proxy: {
      '/api': 'http://localhost:8080',
    },
  },
})
