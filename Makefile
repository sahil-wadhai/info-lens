# --- Developer Automation Tools (Profile-Driven) ---

# CRITICAL FIX: Explicitly tells Make to run the 'help' target if no argument is passed
.DEFAULT_GOAL := help


.PHONY: dev-up prod-up stop start down clean help

# 1. INITIAL SPIN-UP (DEV MODE): Starts only Postgres, Redis, Kafka for local IDE coding
dev-up:
	@echo "🚀 Spinning up local dev dependencies via profile environment variable..."
	COMPOSE_PROFILES=dev docker compose up -d
	@echo "✅ Dev infrastructure is live on localhost! Run your app via your IDE."

# 2. INITIAL SPIN-UP (PROD MODE): Compiles Java 25 code and launches the entire stack
prod-up:
	@echo "📦 Compiling code and launching complete local production stack..."
	COMPOSE_PROFILES=prod docker compose up -d --build
	@echo "🚀 Full application stack is live! App accessible at http://localhost:8080"

# 3. PAUSE CONTAINERS: Freezes active containers in place without deleting them (Saves RAM/Battery)
stop:
	@echo "🛑 Pausing active environment containers..."
	COMPOSE_PROFILES=dev,prod docker compose stop
	@echo "⏸️  Containers paused. Wake them up anytime with 'make start'."

# 4. RESUME CONTAINERS: Instantly wakes up paused containers in milliseconds
start:
	@echo "▶️  Resuming paused containers..."
	COMPOSE_PROFILES=dev,prod docker compose start
	@echo "⚡ Containers are back online!"

# 5. DESTROY CONTAINERS: Stops and completely deletes container runtimes and networks
down:
	@echo "🧹 Stopping and completely removing all environment containers..."
	COMPOSE_PROFILES=dev,prod docker compose down
	@echo "✨ Workspace clean."

# 6. CLEANING: Deletes Maven build targets on your physical machine
clean:
	@echo "🧼 Cleaning local target folders..."
	mvn clean

# 7. AUTOMATED HELP MENU: Displays options if a developer runs a plain 'make' command
help:
	@echo "Available automation shortcuts:"
	@echo "  make dev-up   - Boot infrastructure only using COMPOSE_PROFILES=dev"
	@echo "  make prod-up  - Boot full stack using COMPOSE_PROFILES=prod"
	@echo "  make stop     - Pause running containers without losing operational state"
	@echo "  make start    - Instantly resume paused containers"
	@echo "  make down     - Stop and completely remove all container runtimes"
	@echo "  make clean    - Run local maven clean"
