package org.boreas.common.login.userdetail;

import java.util.Collection;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

/**
 * Extension class of {@link User}. Enable to save more attributes of user.
 * 
 * @author boreas
 *
 * @param <T>
 *            The user-defined <b>user bean</b> class, must implement
 *            {@code PasswordProtector} interface. The object will be fulfilled
 *            by {@link BeanPropertyRowMapper}
 */
public class ExtendUser<T extends PasswordProtector> extends User {

	/**
	 * Generated UID.
	 * 
	 * @since 1.0
	 */
	private static final long serialVersionUID = -2545232424800142426L;

	private T fullDetail;

	public ExtendUser(String username, String password,
			Collection<? extends GrantedAuthority> authorities) {
		super(username, password, true, true, true, true, authorities);
	}

	public ExtendUser(String username, String password, boolean enabled,
			boolean accountNonExpired, boolean credentialsNonExpired,
			boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired,
				credentialsNonExpired, accountNonLocked, authorities);
	}

	public T getFullDetail() {
		return fullDetail;
	}

	public void setFullDetail(T fullDetail) {
		this.fullDetail = fullDetail;
	}

}
