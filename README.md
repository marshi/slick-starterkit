# slick-starterkit

slick 3系を使うための設定とかクエリとかのサンプル.

後述のDDLを実行してローカルでPostgreSQLを動かしている前提.

DBはPostgreSQLの想定.

select, insert, update, deleteをひと通り.

コネクションプーリングにはHikariCPを使用.

## application.conf
typesafe configを使用.

postgreSQLのユーザ名、パスワードは共に"postgres"

## src/main/scala/CodeGen.scala
実行するとDBのテーブル情報を取得して自動的にテーブルに対応したエンティティのコードを生成してくれる.
このプロジェクトでは既にentityパッケージに生成してあるので実行不要だが、テーブルの内容を更新した場合に実行するとよい.

## DDL
```
CREATE TABLE host_branch
(
  id serial NOT NULL,
  branch_name character varying(128),
  host_machine_id integer NOT NULL,
  deploy_time timestamp with time zone,
  CONSTRAINT id PRIMARY KEY (id),
  CONSTRAINT host_branch_host_machine_id_key UNIQUE (host_machine_id)
)
```

```
CREATE TABLE host_machine
(
  id serial NOT NULL,
  name character varying(64) NOT NULL,
  CONSTRAINT host_machine_pkey PRIMARY KEY (id),
  CONSTRAINT host_machine_name_key UNIQUE (name)
)
```
