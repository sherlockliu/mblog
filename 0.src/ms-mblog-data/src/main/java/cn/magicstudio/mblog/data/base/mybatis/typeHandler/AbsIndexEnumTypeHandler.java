package cn.magicstudio.mblog.data.base.mybatis.typeHandler;

import com.yougou.logistics.base.common.enums.IndexEnum;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

public abstract class AbsIndexEnumTypeHandler extends
		BaseTypeHandler<IndexEnum<?>> {
	private Class<IndexEnum<?>> type;
	private IndexEnum<?>[] instanceList;

	public AbsIndexEnumTypeHandler() {
		this.type = ((org.apache.ibatis.type.MappedTypes) (org.apache.ibatis.type.MappedTypes) getClass()
				.getAnnotations()[0]).value()[0];
		this.instanceList = ((IndexEnum[]) this.type.getEnumConstants());
	}

	public IndexEnum<?> getNullableResult(ResultSet rs, String colName)
			throws SQLException {
		return convert(rs.getInt(colName));
	}

	public IndexEnum<?> getNullableResult(ResultSet rs, int colIndex)
			throws SQLException {
		return convert(rs.getInt(colIndex));
	}

	public IndexEnum<?> getNullableResult(CallableStatement cs, int colIndex)
			throws SQLException {
		return convert(cs.getInt(colIndex));
	}

	public void setNonNullParameter(PreparedStatement ps, int colIndex,
			IndexEnum<?> param, JdbcType type) throws SQLException {
		ps.setInt(colIndex, param.getIndex());
	}

	private IndexEnum<?> convert(int index) {
		for (IndexEnum<?> value : this.instanceList) {
			if (value.getIndex() == index) {
				return value;
			}
		}
		return null;
	}
}
