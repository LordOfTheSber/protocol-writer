-- Переход от секций (JSONB) к единому markdown-документу.
ALTER TABLE protocols ADD COLUMN body TEXT NOT NULL DEFAULT '';
ALTER TABLE protocols DROP COLUMN content;
