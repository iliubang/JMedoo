package cn.iliubang.jmedoo.test;

import cn.iliubang.jmedoo.SqlBuilder;
import cn.iliubang.jmedoo.entity.Query;
import cn.iliubang.jmedoo.test.entity.User;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * {Insert class description here}
 *
 * @author <a href="mailto:liubang@staff.weibo.com">liubang</a>
 * @version $Revision: {Version} $ $Date: 2018/5/31 11:10 $
 * @see
 */


public class TestBuilder {

    private static String readFile(String file) throws Exception {
        InputStreamReader inputStreamReader = new InputStreamReader(TestBuilder.class.getResourceAsStream(file), "UTF-8");
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line;
        StringBuilder sb = new StringBuilder();
        while ((line = bufferedReader.readLine()) != null) {
            sb.append(line);
        }

        return sb.toString();
    }

    @Test
    public void testSelect() throws Exception {
        String select = readFile("/select.json");
        Query query = JSON.parseObject(select, Query.class);
        SqlBuilder.SqlObjects sqlObjects = new SqlBuilder().buildSelect("tableA", query);
        System.out.println(sqlObjects);
        Assert.assertEquals("SqlBuilder.SqlObjects(sql=SELECT * FROM \"table_a\" WHERE ((\"or12\" = ? OR \"or11\" < ?) AND ((\"or3and12\" = ? AND \"or3and11\" = ?) OR (\"or3and21\" = ? AND \"or3and22\" = ?)) AND (\"or21\" = ? OR \"or22\" = ?)) AND (\"and21\" != ? AND \"and22\" = ?) AND \"outdate_time\" BETWEEN (?,?) AND \"table_a\".\"update_time\" > ? ORDER BY \"name\" ASC, \"id\" DESC LIMIT 12, 34, objects=[or12, or11, or3and12, or3and11, or3and21, or3and22, or21, or22, and21, and22, t1, t2, 2018-12-21 12:12:12])", sqlObjects.toString());
    }

    @Test
    public void testSelect1() throws Exception {
        String select = readFile("/select.json");
        String column = readFile("/column.json");
        String join = readFile("/join.json");
        Query query = JSON.parseObject(select, Query.class);
        @SuppressWarnings("unchecked")
        Map<String, Object> joinTable = (Map<String, Object>) JSON.parseObject(join, Map.class);
        @SuppressWarnings("unchecked")
        List<Object> col = (List<Object>) JSON.parseObject(column, ArrayList.class);
        SqlBuilder.SqlObjects sqlObjects = new SqlBuilder().buildSelect("test", joinTable, col, query);
        System.out.println(sqlObjects);
        Assert.assertEquals("SqlBuilder.SqlObjects(sql=SELECT \"table_a\".\"column1\" AS \"tac1\", \"table_a\".*, \"table_b\".\"column1\" AS \"tbc1\", \"table_b\".\"column2\" AS \"tbc2\" FROM \"test\" INNER JOIN \"table_d\" USING (\"tdc1\") LEFT JOIN \"table_e\" ON \"table_d\".\"table_dc1\" = \"table_e\".\"table_ec1\" AND \"table_d\".\"table_dc2\" = \"table_e\".\"maste_ec2\" LEFT JOIN \"table_b\" USING (\"tbc1\") RIGHT JOIN \"table_a\" USING (\"tac1\") FULL JOIN \"table_c\" USING (\"tcc1\") WHERE ((\"or12\" = ? OR \"or11\" < ?) AND ((\"or3and12\" = ? AND \"or3and11\" = ?) OR (\"or3and21\" = ? AND \"or3and22\" = ?)) AND (\"or21\" = ? OR \"or22\" = ?)) AND (\"and21\" != ? AND \"and22\" = ?) AND \"outdate_time\" BETWEEN (?,?) AND \"table_a\".\"update_time\" > ? ORDER BY \"name\" ASC, \"id\" DESC LIMIT 12, 34, objects=[or12, or11, or3and12, or3and11, or3and21, or3and22, or21, or22, and21, and22, t1, t2, 2018-12-21 12:12:12])", sqlObjects.toString());
    }

    @Test
    public void testGroup() throws Exception {
        String group = readFile("/group.json");
        Query query = JSON.parseObject(group, Query.class);

        SqlBuilder.SqlObjects sqlObjects = new SqlBuilder().buildSelect("test", query);
        System.out.println(sqlObjects.toString());
        Assert.assertEquals("SqlBuilder.SqlObjects(sql=SELECT * FROM \"test\" WHERE \"name\" IN (?,?,?) GROUP BY \"name\", \"id\" ORDER BY \"name\" ASC, \"id\" DESC LIMIT 12, 34, objects=[e, b, c])", sqlObjects.toString());
    }

    @Test
    public void testGroup1() throws Exception {
        String group = readFile("/group1.json");
        Query query = JSON.parseObject(group, Query.class);

        SqlBuilder.SqlObjects sqlObjects = new SqlBuilder().buildFuncQuery(SqlBuilder.QueryFunc.COUNT, "test", null, Pair.of("id", "c"), query);
        System.out.println(sqlObjects.toString());
        Assert.assertEquals("SqlBuilder.SqlObjects(sql=SELECT COUNT(\"id\") AS \"c\" FROM \"test\" GROUP BY \"type\" HAVING \"c\" > ?, objects=[10])", sqlObjects.toString());
    }

    @Test
    public void testCount() throws Exception {
        String select = readFile("/select.json");
        Query query = JSON.parseObject(select, Query.class);
        SqlBuilder.SqlObjects sqlObjects = new SqlBuilder().buildFuncQuery(SqlBuilder.QueryFunc.COUNT, "tableA", null, null, query);
        System.out.println(sqlObjects);
        Assert.assertEquals("SqlBuilder.SqlObjects(sql=SELECT COUNT(*) FROM \"table_a\" WHERE ((\"or12\" = ? OR \"or11\" < ?) AND ((\"or3and12\" = ? AND \"or3and11\" = ?) OR (\"or3and21\" = ? AND \"or3and22\" = ?)) AND (\"or21\" = ? OR \"or22\" = ?)) AND (\"and21\" != ? AND \"and22\" = ?) AND \"outdate_time\" BETWEEN (?,?) AND \"table_a\".\"update_time\" > ? ORDER BY \"name\" ASC, \"id\" DESC LIMIT 12, 34, objects=[or12, or11, or3and12, or3and11, or3and21, or3and22, or21, or22, and21, and22, t1, t2, 2018-12-21 12:12:12])", sqlObjects.toString());
    }

    @Test
    public void testCount1() throws Exception {
        String select = readFile("/select.json");
        String join = readFile("/join.json");
        Query query = JSON.parseObject(select, Query.class);
        @SuppressWarnings("unchecked")
        Map<String, Object> joinTable = (Map<String, Object>) JSON.parseObject(join, Map.class);
        SqlBuilder.SqlObjects sqlObjects = new SqlBuilder().buildFuncQuery(SqlBuilder.QueryFunc.COUNT, "test", joinTable, Pair.of("testColumn", null), query);
        System.out.println(sqlObjects);
        Assert.assertEquals("SqlBuilder.SqlObjects(sql=SELECT COUNT(\"test_column\") FROM \"test\" INNER JOIN \"table_d\" USING (\"tdc1\") LEFT JOIN \"table_e\" ON \"table_d\".\"table_dc1\" = \"table_e\".\"table_ec1\" AND \"table_d\".\"table_dc2\" = \"table_e\".\"maste_ec2\" LEFT JOIN \"table_b\" USING (\"tbc1\") RIGHT JOIN \"table_a\" USING (\"tac1\") FULL JOIN \"table_c\" USING (\"tcc1\") WHERE ((\"or12\" = ? OR \"or11\" < ?) AND ((\"or3and12\" = ? AND \"or3and11\" = ?) OR (\"or3and21\" = ? AND \"or3and22\" = ?)) AND (\"or21\" = ? OR \"or22\" = ?)) AND (\"and21\" != ? AND \"and22\" = ?) AND \"outdate_time\" BETWEEN (?,?) AND \"table_a\".\"update_time\" > ? ORDER BY \"name\" ASC, \"id\" DESC LIMIT 12, 34, objects=[or12, or11, or3and12, or3and11, or3and21, or3and22, or21, or22, and21, and22, t1, t2, 2018-12-21 12:12:12])", sqlObjects.toString());
    }

    @Test
    public void testLimit() throws Exception {
        String limit = readFile("/limit.json");
        Query query = JSON.parseObject(limit, Query.class);
        SqlBuilder.SqlObjects sqlObjects = new SqlBuilder().buildSelect("tableA", query);
        System.out.println(sqlObjects.toString());
        Assert.assertEquals("SqlBuilder.SqlObjects(sql=SELECT * FROM \"table_a\" LIMIT 1, 20, objects=[])", sqlObjects.toString());
    }

    @Test
    public void testOrder() throws Exception {
        String order = readFile("/order.json");
        Query query = JSON.parseObject(order, Query.class);
        SqlBuilder.SqlObjects sqlObjects = new SqlBuilder().buildSelect("tableA", query);
        System.out.println(sqlObjects);
        Assert.assertEquals("SqlBuilder.SqlObjects(sql=SELECT * FROM \"table_a\" ORDER BY \"id\" DESC, \"time\" ASC, FIELD (\"status\", ?, ?, ?, ?), objects=[3, 2, 1, 5])", sqlObjects.toString());
    }

    @Test
    public void testNotIn() throws Exception {
        String notin = readFile("/notin.json");
        Query query = JSON.parseObject(notin, Query.class);
        SqlBuilder.SqlObjects sqlObjects = new SqlBuilder().buildSelect("tableA", query);
        System.out.println(sqlObjects);
        Assert.assertEquals("SqlBuilder.SqlObjects(sql=SELECT * FROM \"table_a\" WHERE (\"bbb\" IN (?,?) OR \"aaa\" NOT IN (?,?,?)), objects=[c, d, a, b, c])", sqlObjects.toString());
    }

    @Test
    public void testLike() throws Exception {
        String like = readFile("/like.json");
        Query query = JSON.parseObject(like, Query.class);
        SqlBuilder.SqlObjects sqlObjects = new SqlBuilder().buildSelect("tableA", query);
        System.out.println(sqlObjects);
        Assert.assertEquals("SqlBuilder.SqlObjects(sql=SELECT * FROM \"table_a\" WHERE \"aaa\" LIKE '%' ? '%' AND \"ccc\"\" NOT LIKE '%' ? '%', objects=[bbb, ddd])", sqlObjects.toString());
    }

    @Test
    public void testWhere() throws Exception {
        String where = readFile("/where.json");
        Query query = JSON.parseObject(where, Query.class);
        try {
            SqlBuilder.SqlObjects sqlObjects = new SqlBuilder().buildSelect("tableA", query);
        } catch (Exception e) {
            Assert.assertEquals(e.getMessage(), "Sql parsing error: bad column (bizId\"=1 and \"'uid[<>])");
        }
    }

    @Test
    public void testInsert4Update() {
        User user = new User();
        user.setUid(1);
        user.setUname("liubang");
        user.setDesc("a good man!");
        SqlBuilder.SqlObjects sqlObjects = new SqlBuilder().buildInsertForUpdate("user", User.class, user);
        System.out.println(sqlObjects);
        Assert.assertEquals("SqlBuilder.SqlObjects(sql=INSERT INTO \"user\" (\"uname\", \"desc\" ) VALUES (?,?)  ON DUPLICATE KEY UPDATE \"uname\" = ?, \"desc\" = ? , objects=[liubang, a good man!, liubang, a good man!])", sqlObjects.toString());
    }
}
