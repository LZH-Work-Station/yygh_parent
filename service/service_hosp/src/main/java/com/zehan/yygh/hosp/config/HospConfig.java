package com.zehan.yygh.hosp.config;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.zehan.yygh.hosp.mapper")  // 省去了每个 mapper上面都需要@Mapper这一步
public class HospConfig {
}
