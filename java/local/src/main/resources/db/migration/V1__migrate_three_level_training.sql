-- 三级教育培训表迁移脚本
-- 1. 检查是否存在旧表
SET @table_exists = (SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = DATABASE() AND table_name = 'three_level_training');

-- 2. 如果旧表存在，则创建新表并迁移数据
SET @sql = IF(@table_exists > 0, 
    'CREATE TABLE IF NOT EXISTS `biz_three_level_training` LIKE `three_level_training`;
     INSERT INTO `biz_three_level_training` SELECT * FROM `three_level_training`;',
    'SELECT "旧表不存在，无需迁移" AS message');

-- 3. 执行动态SQL
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 4. 如果旧表存在，可以选择删除旧表（根据实际情况决定是否执行）
-- SET @drop_sql = IF(@table_exists > 0, 'DROP TABLE `three_level_training`', 'SELECT "旧表不存在，无需删除" AS message');
-- PREPARE drop_stmt FROM @drop_sql;
-- EXECUTE drop_stmt;
-- DEALLOCATE PREPARE drop_stmt; 