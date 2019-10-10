CREATE TABLE commodity_category (
  commodity_id VARCHAR(40) NOT NULL DEFAULT '',
  category_id VARCHAR(40) NOT NULL DEFAULT '',
  program_id VARCHAR(4000) DEFAULT '',
  create_date_time TIMESTAMP,
  create_user_id VARCHAR(200) DEFAULT '',
  PRIMARY KEY (commodity_id,category_id)
) WITH (OIDS=TRUE);