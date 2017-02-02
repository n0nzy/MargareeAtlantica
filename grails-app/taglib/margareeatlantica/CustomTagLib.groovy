package margareeatlantica

import java.util.Random;
import org.codehaus.groovy.grails.web.mapping.LinkGenerator

class CustomTagLib {
	static namespace = "ma"
	
	// Inject link generator
	LinkGenerator grailsLinkGenerator
	
	static Random random = new Random()
	static List<String> greetings = [
			"Welcome",
			"جی آیاں نُوں",
			"Welkom",
			"Mirë se vjen",
			"Wellkumma",
			"in kwahn deh-na meh tah",
			"أهلاً و سهلاً",
			"Ahla w sahla",
			"Bienveniu",
			"Բարի գալուստ",
			"Բարի՜ եկաք",
			"Ghini vinishi",
			"আদৰিণ",
			"Bienveníu",
			"Xoş gəlmişsiniz",
			"Salamat datang",
			"Ongi etorri",
			"Menjuah-juah! Horas",
			"Вiтаем",
			"Прывiтанне",
			"স্বাগতম",
			"स्वागत बा",
			"Dobrodošli",
			"Degemer mat",
			"Добре дошъл",
			"Добре заварил",
			"Добре дошло",
			"Benvingut",
			"Maayong pag-abot",
			"Bien binidu",
			"марша вагIийла хьо",
			"марша дагIийла шу",
			"ᎤᎵᎮᎵᏍᏗ",
			"歡迎",
			"歡迎光臨",
			"欢迎",
			"歡迎光臨",
			"Dynnargh dhis",
			"Vítáme tĕ",
			"Velkommen",
			"Welkom",
			"A me di o",
			"Bonvenon",
			"Tere tulemast",
			"Woezor",
			"Vælkomin",
			"Tervetuloa",
			"Bienvenue",
			"Wäljkiimen",
			"Wolkom",
			"Agradît",
			"Benvido",
			"Willkommen",
			"კეთილი იყოს თქვენი",
			"Καλώς Ορίσατε",
			"Tikilluarit",
			"Eguahé porá",
			"પધારો",
			"Byen venu",
			"Sannu da zuwa",
			"Aloha",
			"ברוך הבא",
			"सवागत हैं",
			"Velkomin",
			"Selamat datang",
			"Qaimarutin",
			"Fáilte romhat isteach!",
			"Céad míle fáilte",
			"Benvenuto",
			"Sugeng rawuh",
			"ಸುಸ್ವಾಗತ",
			"Қош келдіңіз",
			"សូម​ស្វាគមន៍",
			"Murakaza neza",
			"환영합니다",
			"Kosh kelinizder",
			"Taŋyáŋ yahí",
			"ຍິນດີຕ້ອນຮັບ",
			"Salve",
			"Laipni lūdzam",
			"Wilkóm",
			"Sveiki atvykę",
			"Mu amuhezwi",
			"Wëllkomm",
			"സ്വാഗതം",
			"Merħba",
			"Haere mai",
			"स्वागत आहे",
			"خش به‌مونی",
			"Weltasualuleg",
			"Тавтай морилогтун",
			"Ximopanōltih",
			"Siyalemukela",
			"स्वागतम्",
			"लसकुस",
			"Velkommen",
			"Benvengut",
			"Swaagata",
			"Bon bini",
			"پخير‏",
			"خوش آمدید",
			"Bem-vindo",
			"Haykuykuy",
			"Bainvegni",
			"Добро пожаловать",
			"Ennidos",
			"Fàilte",
			"Добродошли",
			"Kena ka kgotso",
			"Mauya",
			"Binvinutu",
			"සාදරයෙන් පිලිගන්නවා",
			"Vitaj",
			"Dobrodošli",
			"Välkommen",
			"Wilkomme",
			"Maligayang pagdating",
			"Maeva",
			"வாங்க",
			"Räxim itegez",
			"సుస్వాగతం",
			"ยินดีต้อนรับ",
			"መርሓባ (merhaba)",
			"Xush kelibsiz",
			"Benvignùo",
			"Vekömö",
			"Tere tulõmast",
			"Benvnuwe",
			"Croeso",
			"Merhbe",
			"Siya namkela nonke",
			"Ngiyakwemukela"
		]
	
	def pageTitle = { attrs, body ->
		String title = attrs.title
		String subTitle = attrs.subTitle
		if (!title) title = "Page"
		if (!subTitle) subTitle = "title"
		out << "<div class=\"span8\"><h2 class=\"page-title\">" << title << " <small>" << subTitle << "</small></h2></div>"
	}
	
	def greetUser = { attrs, body ->
		String greeting = greetings.get(random.nextInt(greetings.size()))
		String userName = attrs.name
		if (!userName) userName = "User"
		out << "<div class=\"span8\"><h2 class=\"page-title\">" << greeting << " <small>" << userName << "!</small></h2></div>"
	}
	
	def pageSideNavigation = { attrs, body ->
		AuthService authService = AuthService.getInstance()
		AuthUser user = authService.getSessionUser()
		Integer role = user.role
		if (role == 0) role = AuthService.ROLE_CUSTOMER
		String currController = params.controller
		String currAction = params.action
		if (!currController) currController = "index"
		if (!currAction) currAction = "index"
		
		out << "<nav id=\"sidebar\" class=\"sidebar nav-collapse collapse\">\n"
		out << "<ul id=\"side-nav\" class=\"side-nav\">\n"
		if (user.role == AuthService.ROLE_CUSTOMER) {
			// display Customer menu
			out << "<li" << genLiActive(currController,currAction,"customer","index") << "><a href=\"" << grailsLinkGenerator.link(controller: 'customer', action: 'index') << "\"><i class=\"icon-desktop\"></i> <span class=\"name\">Dashboard</span></a></li>\n"
			out << "<li" << genLiActive(currController,currAction,"customer","placeOrder") << "><a href=\"" << grailsLinkGenerator.link(controller: 'customer', action: 'placeOrder') << "\"><i class=\"icon-shopping-cart\"></i> <span class=\"name\">Place Order</span></a></li>\n"
			out << "<li" << genLiActive(currController,currAction,"customer","proposeProduct") << "><a href=\"" << grailsLinkGenerator.link(controller: 'customer', action: 'proposeProduct') << "\"><i class=\"icon-gift\"></i> <span class=\"name\">Propose Product</span></a></li>\n"
		} else if (user.role == AuthService.ROLE_CUSTOMER_RELATIONSHIP_MANAGER) {
			out << "<li" << genLiActive(currController,currAction,"crm","index") << "><a href=\"" << grailsLinkGenerator.link(controller: 'crm', action: 'index') << "\"><i class=\"icon-desktop\"></i> <span class=\"name\">Dashboard</span></a></li>\n"
			out << "<li" << genLiActive(currController,currAction,"crm","upgradeCustomer") << "><a href=\"" << grailsLinkGenerator.link(controller: 'crm', action: 'upgradeCustomer') << "\"><i class=\"icon-user\"></i> <span class=\"name\">Upgrade Customer</span></a></li>\n"
			out << "<li" << genLiActive(currController,currAction,"crm","releaseOffer") << "><a href=\"" << grailsLinkGenerator.link(controller: 'crm', action: 'releaseOffer') << "\"><i class=\"icon-user\"></i> <span class=\"name\">Release Offer</span></a></li>\n"
		} else if (user.role == AuthService.ROLE_ORDER_MANAGER) {
			out << "<li" << genLiActive(currController,currAction,"om","index") << "><a href=\"" << grailsLinkGenerator.link(controller: 'om', action: 'index') << "\"><i class=\"icon-desktop\"></i> <span class=\"name\">Dashboard</span></a></li>\n"
			out << "<li" << genLiActive(currController,currAction,"om","analyzeProposals") << "><a href=\"" << grailsLinkGenerator.link(controller: 'om', action: 'analyzeProposals') << "\"><i class=\"icon-user\"></i> <span class=\"name\">Analyze Proposals</span></a></li>\n"
			out << "<li" << genLiActive(currController,currAction,"om","procureProduct") << "><a href=\"" << grailsLinkGenerator.link(controller: 'om', action: 'procureProduct') << "\"><i class=\"icon-user\"></i> <span class=\"name\">Procure Product</span></a></li>\n"
			out << "<li" << genLiActive(currController,currAction,"om","clearOrders") << "><a href=\"" << grailsLinkGenerator.link(controller: 'om', action: 'clearOrders') << "\"><i class=\"icon-user\"></i> <span class=\"name\">Clear Orders</span></a></li>\n"
		} else {
			out << "<li" << genLiActive(currController,currAction,"pm","index") << "><a href=\"" << grailsLinkGenerator.link(controller: 'pm', action: 'index') << "\"><i class=\"icon-desktop\"></i> <span class=\"name\">Dashboard</span></a></li>\n"
			out << "<li" << genLiActive(currController,currAction,"pm","launchProduct") << "><a href=\"" << grailsLinkGenerator.link(controller: 'pm', action: 'launchProduct') << "\"><i class=\"icon-user\"></i> <span class=\"name\">Launch Product</span></a></li>\n"
			out << "<li" << genLiActive(currController,currAction,"pm","retireProduct") << "><a href=\"" << grailsLinkGenerator.link(controller: 'pm', action: 'retireProduct') << "\"><i class=\"icon-user\"></i> <span class=\"name\">Retire Product</span></a></li>\n"
		}
		out << "<li" << genLiActive(currController,currAction,"user","index") << "><a href=\"" << grailsLinkGenerator.link(controller: 'user', action: 'index') << "\"><i class=\"icon-user\"></i> <span class=\"name\">Edit Profile</span></a></li>\n"
		out << "<li><a href=\"" << grailsLinkGenerator.link(controller: 'user', action: 'logout') << "\"><i class=\"icon-signout\"></i> <span class=\"name\">Logout</span></a></li>\n"
		out << "</ul></nav>"
	}
	
	protected static String genLiActive(String curr_cont, String curr_act, String target_cont, String target_act) {
		if (curr_cont.equalsIgnoreCase(target_cont) && curr_act.equalsIgnoreCase(target_act)) {
			return " class=\"active\""
		}
		return ""
	}
	
	def pageContentHeader = { attrs, body ->
		AuthService authService = AuthService.getInstance()
		AuthUser user = authService.getSessionUser()
		
		out << "<div class=\"navbar\"><div class=\"navbar-inner\">"
		out << "<ul class=\"nav pull-right\">\n"
		out << "<li class=\"dropdown\">"
		out << "<a href=\"#\" title=\"Account\" id=\"account\" class=\"dropdown-toggle\" data-toggle=\"dropdown\"> <i class=\"icon-user\"></i> "
		out << "&nbsp;account &nbsp;&nbsp;&nbsp; </a>\n"
		out << "<ul id=\"account-menu\" class=\"dropdown-menu account\" role=\"menu\">\n"
		out << "<li role=\"presentation\" class=\"account-picture\">\n"
		out << "<img src=\"img/2.jpg\" alt=\"\" />\n"
		out << user.name << "\n"
		out << "</li>\n"
		out << "<li role=\"presentation\"><a href=\"" << grailsLinkGenerator.link(controller: 'user', action: 'index') << "\" class=\"link\"> <i class=\"icon-user\"></i> Edit Profile</a></li>\n"
		out << "</ul></li>\n"
		out << "<li><a href=\"" << grailsLinkGenerator.link(controller: 'user', action: 'logout') << "\"><i class=\"icon-signout\"></i></a></li></ul>\n"
		out << "</div></div>"
	}
}
