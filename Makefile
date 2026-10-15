SHELL := /bin/bash

COMPOSE := cd test-server && docker compose

.PHONY: help build start stop restart deploy logs clean

help: ## 使えるコマンド一覧を表示
	@awk 'BEGIN {FS = ":.*## "; print "Usage: make <target>"} \
		/^[a-zA-Z0-9_-]+:.*## / {printf "  %-20s %s\n", $$1, $$2}' $(MAKEFILE_LIST)

build: ## jarをビルド (build/libs/)
	./gradlew build

start: ## テストサーバを起動 (バックグラウンド)
	$(COMPOSE) up -d

stop: ## テストサーバを停止 (データは保持)
	$(COMPOSE) stop

restart: ## テストサーバを再起動 (プラグイン再読込)
	$(COMPOSE) restart

deploy: build ## ビルドしてjarをplugins/へ配置し再起動
	./gradlew deployToTestServer
	$(COMPOSE) restart

logs: ## テストサーバのログを追跡
	$(COMPOSE) logs -f

clean: ## テストサーバを削除してデータをリセット
	$(COMPOSE) down
	rm -rf test-server/data
