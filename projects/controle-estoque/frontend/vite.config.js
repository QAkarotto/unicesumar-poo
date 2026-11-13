import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// Em desenvolvimento, as chamadas para /api são redirecionadas ao Spring Boot (porta 8080).
export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    proxy: {
      '/api': 'http://localhost:8080',
    },
  },
})
