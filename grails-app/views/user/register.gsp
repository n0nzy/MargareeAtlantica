<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
<meta name="layout" content="plain"/>
<title>Enroll Customer</title>
</head>
<body>
    <section class="widget registration-widget" style="width:700px">
        <header class="text-align-center">
            <h4>Enroll as a Customer</h4>
        </header>
        <div class="body">
            <g:each var="error" in="${errors}"><p>ERROR: <strong>${error}</strong></p></g:each>
            <form class="no-margin" action="${createLink(controller:'user',action:'register')}" method="POST" novalidate="novalidate" data-validate="parsley" />
                <fieldset>
                    <legend class="section">Personal Info</legend>
                    <div class="control-group">
                        <div class="controls controls-row">
                            <input style="width:320px;margin-right:15px" type="text" placeholder="Your Name" id="name" name="name" data-trigger="change" maxlength="20" required="required" value="${params['name']}" />
                            <input style="width:320px" type="text" placeholder="Your Address" id="address" name="address" data-trigger="change" maxlength="30" required="required" value="${params['address']}" />
                        </div>
                    </div>
                    <div class="control-group">
                        <div class="controls controls-row">
                            <input style="width:320px;margin-right:15px" type="text" placeholder="Your Home Phone" id="resident_phone" name="resident_phone" data-trigger="change" maxlength="12" required="required" value="${params['resident_phone']}" />
                            <input style="width:320px" type="text" placeholder="Your Mobile Phone" id="mobile_phone" name="mobile_phone" data-trigger="change" maxlength="12" required="required" value="${params['mobile_phone']}" />
                        </div>
                    </div>
                    <div class="control-group">
                        <div class="controls controls-row">
                            <input style="width:320px;margin-right:15px" type="email" placeholder="Your Email Address" id="email_id" name="email_id" data-trigger="change" maxlength="25" required="required" data-validation-minlength="1" value="${params['email_id']}" />
                            <select id="country_id" data-placeholder="Select Your Country" required="required" class="chzn-select" style="width:320px" name="country_id">
                                <option value=""></option>
                                <g:each var="country_id" in="${countries.keySet()}">
                                <option value="${country_id}" <g:if test="${country_id.toString().equals(params['country_id'])}">selected</g:if> >${countries[country_id]}</option>
                                </g:each>
                            </select>
                        </div>
                    </div>
                    <legend class="section">Login Credentials</legend>
                    <div class="control-group no-margin">
                        <div class="control">
                            <div class="input-prepend input-padding-increased">
                                <span class="add-on">
                                    <i class="eicon-user icon-large"></i>
                                </span>
                                <input style="width:300px;margin-right:15px" id="username" name="username" placeholder="Your Username" type="text" maxlength="15" required="required" value="${params['username']}" />
                                <span class="add-on">
                                    <i class="icon-lock icon-large"></i>
                                </span>
                                <input style="width:300px" id="password" name="password" type="password" placeholder="Your Password" />
                            </div>
                        </div>
                    </div>
                </fieldset>
                <div class="form-actions">
                    <button type="submit" class="btn btn-block btn-large btn-danger">
                        <span class="small-circle"><i class="icon-caret-right"></i></span>
                        <small>Register</small>
                    </button>
                    <div class="forgot"> </div>
                </div>
                <input type="hidden" name="redirectTo" id="redirectTo" value="${params['redirectTo']}"/>
                <input type="hidden" name="submitted" id="submitted" value="true"/>
            </form>
        </div>
    </section>
</body>
</html>