package com.ddy.stock.real.domain;
/**
 * 功能说明:json实体类
 * <p> 系统版本: v1.0<br>
 * 开发人员: luopeng12856
 * 开发时间: 2016—05-26 上午10:12 <br>
 */
public class AccessToken {
	/**
     *授权服务授权颁发的访问令牌.
     */
	private  String access_token;
	/**
     *令牌类型告诉客户端一个信息，即当访问一个受保护资源时，访问令牌应该如何被使用
     */
	private  String token_type;
	/**
     *表示访问令牌生命周期的秒数
     */
	private  String expires_in;
	/**
     *用于换取新的访问令牌的刷新令牌，只有在刷新令牌时必须有此参数
     */
	private  String refresh_token;
	/**
     *用于指定授权访问的范围，多个授权范围可以使用逗号（,）连接
     */
	private  String scope;
	private String auth_id;
	private String fund_account;
	private String client_id;
	private String client_name;
	private String fund_accout;
	/**
	 * @return the fund_accout.
	 */
	public String getFund_accout() {
		return fund_accout;
	}
	/**
	 * @param fund_accout the fund_accout to set.
	 */
	public void setFund_accout(String fund_accout) {
		this.fund_accout = fund_accout;
	}
	/**
	 * @return the auth_id.
	 */
	public String getAuth_id() {
		return auth_id;
	}
	/**
	 * @param auth_id the auth_id to set.
	 */
	public void setAuth_id(String auth_id) {
		this.auth_id = auth_id;
	}
	/**
	 * @return the fund_account.
	 */
	public String getFund_account() {
		return fund_account;
	}
	/**
	 * @param fund_account the fund_account to set.
	 */
	public void setFund_account(String fund_account) {
		this.fund_account = fund_account;
	}
	/**
	 * @return the client_id.
	 */
	public String getClient_id() {
		return client_id;
	}
	/**
	 * @param client_id the client_id to set.
	 */
	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}
	/**
	 * @return the client_name.
	 */
	public String getClient_name() {
		return client_name;
	}
	/**
	 * @param client_name the client_name to set.
	 */
	public void setClient_name(String client_name) {
		this.client_name = client_name;
	}
	/**
	 * @return the access_token.
	 */
	public String getAccess_token() {
		return access_token;
	}
	/**
	 * @param access_token the access_token to set.
	 */
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	/**
	 * @return the token_type.
	 */
	public String getToken_type() {
		return token_type;
	}
	/**
	 * @param token_type the token_type to set.
	 */
	public void setToken_type(String token_type) {
		this.token_type = token_type;
	}
	/**
	 * @return the expires_in.
	 */
	public String getExpires_in() {
		return expires_in;
	}
	/**
	 * @param expires_in the expires_in to set.
	 */
	public void setExpires_in(String expires_in) {
		this.expires_in = expires_in;
	}
	/**
	 * @return the refresh_token.
	 */
	public String getRefresh_token() {
		return refresh_token;
	}
	/**
	 * @param refresh_token the refresh_token to set.
	 */
	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}
	/**
	 * @return the scope.
	 */
	public String getScope() {
		return scope;
	}
	/**
	 * @param scope the scope to set.
	 */
	public void setScope(String scope) {
		this.scope = scope;
	}
}
