/*
 Navicat Premium Data Transfer

 Source Server         : server
 Source Server Type    : MySQL
 Source Server Version : 80026
 Source Host           : localhost:3306
 Source Schema         : server

 Target Server Type    : MySQL
 Target Server Version : 80026
 File Encoding         : 65001

 Date: 19/04/2025 17:40:08
*/
create Database server;
use server;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `user_id` bigint(0) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

CREATE TABLE anime (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '动漫ID',
                       title VARCHAR(100) NOT NULL COMMENT '动漫名字',
                       author VARCHAR(50) COMMENT '作者/制作公司',
                       storage_path VARCHAR(500) NOT NULL COMMENT '视频文件存储路径'
) COMMENT '动漫信息表';

CREATE TABLE comic (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '漫画ID',
                       title VARCHAR(100) NOT NULL COMMENT '漫画名字',
                       author VARCHAR(50) NOT NULL COMMENT '作者',
                       storage_path VARCHAR(500) NOT NULL COMMENT '漫画文件存储路径'
) COMMENT '漫画信息表';

SET FOREIGN_KEY_CHECKS = 1;