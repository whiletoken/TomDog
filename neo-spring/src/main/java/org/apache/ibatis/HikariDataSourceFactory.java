package org.apache.ibatis;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSourceFactory;

public class HikariDataSourceFactory extends UnpooledDataSourceFactory {

    public HikariDataSourceFactory() {
        HikariConfig config = new HikariConfig("/hikariPool.properties");
        config.setMaximumPoolSize(5);
        this.dataSource = new HikariDataSource(config);
    }

}
