# Workboard Job Portal

A role-based job platform built with Angular 20, Spring Boot 3.3, MySQL 8, Flyway, and Docker Compose.

## Architecture

- `frontend/`: Angular standalone app with lazy routed pages, typed reactive forms, signals, role guards, and an HTTP auth interceptor.
- `backend/`: Java 17 Spring API with controller/service-style request boundaries, Spring Data repositories, JWT authentication, BCrypt, validation, and Flyway schema migrations.
- `nginx/` and `frontend/nginx.conf`: SPA fallback, API proxy, and security headers.
- `docker-compose.yml`: MySQL, API, and Nginx-served frontend.

## Run with Docker

Copy `.env.example` to `.env`, set strong `DB_PASSWORD`, `DB_ROOT_PASSWORD`, and a randomly generated `JWT_SECRET` of at least 32 bytes, then run. Set `ADMIN_EMAIL` and `ADMIN_PASSWORD` (minimum 12 characters) in `.env` to bootstrap the first admin account.

```sh
docker compose up --build
```

Open [http://localhost](http://localhost). API docs are at [http://localhost/swagger-ui.html](http://localhost/swagger-ui.html).

## Run locally

Start a MySQL 8 database and provide `DB_URL`, `DB_USER`, `DB_PASSWORD`, and `JWT_SECRET` to the backend, then:

```sh
cd backend && mvn spring-boot:run
cd frontend && npm ci && npm start
```

Angular dev-server requests should be proxied to `http://localhost:8080` when running outside Compose. Set `CORS_ORIGINS=http://localhost:4200`.

## Configuration

See `.env.example` for all runtime variables: database settings, JWT signing secret/TTL, bootstrap admin identity, upload directory, and CORS origins. Secrets must be set in the deployment environment.

## API overview

- Auth: `/api/auth/register`, `/api/auth/login`, `/api/auth/me`
- Seeker: `/api/seeker/profile`, `/api/seeker/dashboard`, `/api/seeker/applications`, `/api/seeker/saved-jobs`
- Employer: `/api/employer/company`, `/api/employer/dashboard`, `/api/employer/jobs`
- Public search: `/api/jobs`, `/api/jobs/{id}`, `/api/meta/filters`
- Applications: `/api/jobs/{id}/apply`, `/api/employer/jobs/{id}/applications`

## Verification

```sh
cd backend && mvn verify
cd frontend && npm ci && npm test -- --watch=false --browsers=ChromeHeadless && npm run build
```

## Assumptions and current scope

- Registration defaults to `JOB_SEEKER`; users can self-register only as seekers or employers. Admin is never publicly assignable.
- Employer registration creates a starter company named after the owner; company details can be edited later.
- Job descriptions are accepted as text and stripped of script blocks before storage.
- Current follow-up work: resume upload/download; admin role changes, audit log writes and daily trend metrics; the admin users/companies/jobs management screens; complete search filters (salary, posted window, sort and URL state); detailed application history and status counts; and dev-only demo fixtures.
- No demo credentials are enabled. Admin bootstrap reads `ADMIN_EMAIL` and `ADMIN_PASSWORD` from the environment; no password is hardcoded. For local development, use the test profile with H2 or configure a local MySQL and set `JWT_SECRET`.
