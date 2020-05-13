DROP TABLE IF EXISTS "task";
DROP TABLE IF EXISTS "category";
DROP TABLE IF EXISTS "tag";
DROP TABLE IF EXISTS "task_tag";

CREATE TABLE "task" (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(1023),
    is_done BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE "category" (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255),

    parent INTEGER REFERENCES "category"(id)
);

CREATE TABLE "tag" (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    color VARCHAR(6) NOT NULL DEFAULT 'FFFFFF', --white

    category INTEGER NOT NULL REFERENCES "category"(id)
);

CREATE TABLE "task_tag" (
    task_id INTEGER NOT NULL REFERENCES "task"(id),
    tag_id INTEGER NOT NULL REFERENCES "tag"(id),
    CONSTRAINT task_tag_pkey PRIMARY KEY (task_id, tag_id)
)

