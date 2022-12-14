package bitbucket.neo.mysql;

import lombok.Data;

import java.sql.*;
import java.util.Iterator;
import java.util.Vector;

/**
 * Pool
 *
 * @author liujunjie
 * @version $Id: Pool, v0.1 2018年08月27日 下午4:35  Exp $
 */
public class DataBasePool {

    public static void main(String[] args) {

        DataBasePool dataBasePool = new DataBasePool("com.mysql.cj.jdbc.Driver",
                "jdbc:mysql://47.97.177.55:3306/mysql?useUnicode=true&characterEncoding=UTF-8", "root", "box123456");
        try {
            dataBasePool.createConnections(4);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        Connection conn = dataBasePool.getConnection();
        try {
            String sql = "select * from db";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getString("User"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dataBasePool.returnConnection(conn);
        }
    }

    /**
     * 数据库驱动、数据库url、数据库用户名、数据库密码
     */
    private String jdbcDriver = "", dbUrl = "", dbUsername = "", dbPassword = "";

    private final String testTable = "";

    /**
     * 连接池初始连接数、连接池最大连接数、每次动态添加的连接数
     */
    private final int initialConnectionsNum = 10;
    private int maxConnectionsNum = 50;
    private final int incrementalConnections = 5;

    /**
     * 向量，存放连接池中的连接，初始为空
     */
    private Vector<PooledConnection> connections = null;

    /**
     * 带参数的构造函数
     * 初始化数据库驱动、数据库url、数据库用户名、数据库密码、测试表
     */
    public DataBasePool(String driver, String url, String name, String pass) {
        jdbcDriver = driver;
        dbUrl = url;
        dbUsername = name;
        dbPassword = pass;

        try {
            createPool();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 函数，创建连接池
     */
    public synchronized void createPool()
            throws InstantiationException, IllegalAccessException,
            ClassNotFoundException, SQLException {

        // 确保连接池为创建，如果已经创建，则保存连接的向量不为空
        if (connections != null) {
            return;
        }

        //驱动器实例化
        Driver driver = (Driver) (Class.forName(jdbcDriver).newInstance());

        //注册驱动器
        DriverManager.registerDriver(driver);

        //创建保存连接的向量
        connections = new Vector<>();

        //创建数据库连接
        createConnections(initialConnectionsNum);
    }

    /**
     * 函数，创建数据库连接
     */
    private synchronized void createConnections(int num) throws SQLException {

        //循环创建连接
        //需要首先检查当前连接数是否已经超出连接池最大连接数
        for (int i = 0; i < num; ++i) {

            //检查
            if (connections.size() >= maxConnectionsNum) {
                return;
            }

            //创建连接
            connections.addElement(new PooledConnection(newConnection()));
        }

    }

    /**
     * 函数,创建一个数据库连接
     *
     * @return Connection
     */
    private Connection newConnection() throws SQLException {

        /*创建连接*/
        Connection con = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);

        /*如果是第一次创建连接，则检查所连接的数据库的允许最大连接数是否小于我们所设定的最大连接数*/
        if (connections.size() == 0) {
            DatabaseMetaData metadata = con.getMetaData();

            //得到数据库最大连接数
            int dbMaxConnectionsNum = metadata.getMaxConnections();

            //如果数据库最大连接数更小，则更改我们所设定的连接池最大连接数
            if (dbMaxConnectionsNum > 0 && maxConnectionsNum > dbMaxConnectionsNum) {
                maxConnectionsNum = dbMaxConnectionsNum;
            }
        }
        return con;
    }

    /**
     * 函数，得到一个可用连接
     */
    public synchronized Connection getConnection() {

        Connection con = null;

        /*检查连接池是否已经建立*/
        if (connections == null) {
            return null;
        }

        //得到一个可用连接
        try {
            con = getFreeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //如果未找到合适连接，循环等待、查找，直到找到合适连接
        while (con == null) {
            wait(30);
            try {
                con = getFreeConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return con;
    }


    /**
     * 函数，得到一个可用连接
     *
     * @return Connection
     */
    private Connection getFreeConnection() throws SQLException {

        //查找一个可用连接
        Connection con = findFreeConnection();

        //如果未找到可用连接，就建立一些新的连接，再次查找
        if (con == null) {
            createConnections(incrementalConnections);

            //再次查找
            con = findFreeConnection();
        }
        return con;
    }


    /**
     * 函数，从现有连接中查找一个可用连接
     * 在现有的连接中（向量connections中）找到一个空闲连接，
     * 并测试这个链接是否可用，若不可用则重新建立连接，替换原来的连接
     */
    private Connection findFreeConnection() throws SQLException {
        Connection con = null;
        for (PooledConnection connection : connections) {
            if (connection.isBusy()) {
                continue;
            }

            /*如果此链接未被使用，则返回这个连接并设置正在使用标志*/
            con = connection.getCon();
            connection.setBusy(true);

            /*测试连接是否可用*/
            if (!testCon(con)) {
                con = newConnection();
                connection.setCon(con);
            }
            break;
        }
        return con;
    }

    /**
     * 函数，测试连接是否可用
     */
    private boolean testCon(Connection con) {
        boolean useable = true;
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select count(*) from " + testTable);
            rs.next();
        } catch (SQLException e) {

            /*上面抛出异常，连接不可用，关闭*/
            useable = false;
            closeConnection(con);
        }
        return useable;
    }

    /**
     * 函数，将使用完毕的连接放回连接池中
     */
    public void returnConnection(Connection con) {

        /*确保连接池存在*/
        if (connections == null) {
            return;
        }
        for (PooledConnection pool : connections) {

            //找到相应连接，设置正在使用标志为false
            if (con == pool.getCon()) {
                pool.setBusy(false);
            }
        }

    }

    /**
     * 函数，刷新连接池中的连接
     */
    public synchronized void refreshConnectionPool() throws SQLException {

        /*确保连接池存在*/
        if (connections == null) {
            return;
        }
        for (PooledConnection pool : connections) {
            if (pool.isBusy()) {
                wait(5000);
            }
            closeConnection(pool.getCon());
            pool.setCon(newConnection());
            pool.setBusy(false);
        }
    }

    /**
     * 函数，关闭连接池
     */
    public void closeConnectionPool() {

        /*确保连接池存在*/
        if (connections == null) {
            return;
        }
        Iterator<PooledConnection> iterator = connections.iterator();
        while (iterator.hasNext()) {
            PooledConnection pool = iterator.next();
            if (pool.isBusy()) {
                wait(5000);
            }
            closeConnection(pool.getCon());
            iterator.remove();
        }
        connections = null;
    }

    /**
     * 函数，暂时无可用连接，进入等待队列等待m秒，再试
     */
    private void wait(int mSecond) {
        try {
            Thread.sleep(mSecond);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 函数，连接使用完毕，关闭连接
     *
     * @param con con
     */
    private void closeConnection(Connection con) {
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * 内部使用的保存数据库连接的类
     * 两个成员变量：连接、是否正在使用
     */
    @Data
    static class PooledConnection {

        /**
         * 连接
         */
        private Connection con;

        /**
         * 是否正在使用，默认为非
         */
        private boolean busy = false;

        PooledConnection(Connection con) {
            this.con = con;
        }
    }
}
