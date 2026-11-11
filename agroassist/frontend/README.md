# AgroAssist — frontend

A interface original em React, TypeScript e Vite, integrada à API Spring Boot.

Consulte o [README do monorepo](../README.md) para preparar o MySQL e iniciar o backend.

```bash
npm ci
npm run dev
```

Abra http://localhost:5173. O Vite encaminha `/api` para `http://localhost:8080`.
A configuração opcional `VITE_API_URL` está em `.env.example`.

```bash
npm run build
npm run preview
```

Os dados são lidos da API e persistidos no MySQL. A seleção de usuários é demonstrativa e não implementa login/senha.
