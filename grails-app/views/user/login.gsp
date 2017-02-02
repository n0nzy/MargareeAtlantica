<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
<meta name="layout" content="plain"/>
<title>Login</title>
</head>
<body>
    <section class="widget login-widget">
        <header class="text-align-center">
            <h4>Login to your account</h4>
        </header>
        <div class="body">
            <form class="no-margin" action="${createLink(controller:'user',action:'login')}" method="POST" />
                <fieldset>
                    <div class="control-group no-margin">
                        <label class="control-label" for="username">Username</label>
                        <div class="control">
                            <div class="input-prepend input-padding-increased">
                                <span class="add-on">
                                    <i class="eicon-user icon-large"></i>
                                </span>
                                <input id="username" name="username" type="text" placeholder="Your Username" value="${params['username']}" />
                            </div>
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label" for="password">Password</label>
                        <div class="control">
                            <div class="input-prepend input-padding-increased">
                                <span class="add-on">
                                    <i class="icon-lock icon-large"></i>
                                </span>
                                <input id="password" name="password" type="password" placeholder="Your Password" />
                            </div>
                        </div>
                    </div>
                </fieldset>
                <div class="form-actions">
                    <button type="submit" class="btn btn-block btn-large btn-danger">
                        <span class="small-circle"><i class="icon-caret-right"></i></span>
                        <small>Log In</small>
                    </button>
                    <div class="forgot"></div>
                </div>
                <input type="hidden" name="allowRelogin" id="allowRelogin" value="${allowRelogin}"/>
                <input type="hidden" name="redirectTo" id="redirectTo" value="${params['redirectTo']}"/>
            </form>
        </div>
        <footer>
            <div class="facebook-login">
                <a href="${createLink(controller:'user',action:'register')}"><span><i class="icon-plus icon-large"></i> Register for a Customer Account</span></a>
            </div>
        </footer>
    </section>
</body>
</html>