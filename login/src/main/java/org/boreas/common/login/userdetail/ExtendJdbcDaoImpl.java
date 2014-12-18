package org.boreas.common.login.userdetail;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;

/**
 * Extension of {@link JdbcDaoImpl} for saving more fields of user.
 * 
 * @author boreas
 *
 * @param <T>
 *            The user-defined <b>user bean</b> class, must implement
 *            {@code PasswordProtector} interface.
 */
public class ExtendJdbcDaoImpl<T extends PasswordProtector> extends JdbcDaoImpl
		implements InitializingBean {

	private static final Log log = LogFactory.getLog(ExtendJdbcDaoImpl.class);

	private RowMapper<T> rowMapper;

	/**
	 * Executes the SQL <tt>usersByUsernameQuery</tt> and returns a list of
	 * UserDetails objects. There should normally only be one matching user. <br>
	 * Use {@link BeanPropertyRowMapper} to map all fields in result set to
	 * bean.
	 */
	@Override
	protected List<UserDetails> loadUsersByUsername(String username) {

		return getJdbcTemplate().query(getUsersByUsernameQuery(),
				new String[] { username }, new RowMapper<UserDetails>() {
					public UserDetails mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						String username = rs.getString(1);
						String password = rs.getString(2);
						boolean enabled = rs.getBoolean(3);
						ExtendUser<T> user = new ExtendUser<T>(username,
								password, enabled, true, true, true,
								AuthorityUtils.NO_AUTHORITIES);
						user.setFullDetail(rowMapper.mapRow(rs, rowNum));
						return user;
					}

				});
	}

	/**
	 * Create a user with full details.
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected UserDetails createUserDetails(String username,
			UserDetails userFromUserQuery,
			List<GrantedAuthority> combinedAuthorities) {
		ExtendUser<T> user = new ExtendUser<T>(userFromUserQuery.getUsername(),
				userFromUserQuery.getPassword(), userFromUserQuery.isEnabled(),
				true, true, true, combinedAuthorities);
		T fullDetail = ((ExtendUser<T>) userFromUserQuery).getFullDetail();
		fullDetail.protectPassword();
		user.setFullDetail(fullDetail);
		return user;
	}

	/**
	 * Check if {@code userClassName} indicates a class which implements
	 * {@code PasswordProtector} interface.<br>
	 * Set the value if true, or throw a {@code ClassCastException}.
	 * 
	 * @param userClassName
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public final void setUserClassName(String userClassName)
			throws ClassNotFoundException {
		Class<T> clazz;
		try {
			clazz = (Class<T>) Class.forName(userClassName);
		} catch (ClassNotFoundException e) {
			log.error("Cannot find Class " + userClassName, e);
			throw e;
		}
		if (!PasswordProtector.class.isAssignableFrom(clazz))
			throw new ClassCastException(clazz.getName()
					+ " must implement PasswordProtector interface.");
		rowMapper = new BeanPropertyRowMapper<T>(clazz);
	}
}
