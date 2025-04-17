# vigilant-octo-funicular
汇聚海量热门漫画改编视频，流畅播放，分类精细，一站式视频观看平台。
create database server;
use server;
create table users(
    id bigint primary key auto_increment,
    username varchar(255) not null,
    password varchar(255) not null
)
