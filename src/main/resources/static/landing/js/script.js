(function($) {

	"use strict";

    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    $(document).ajaxSend(function(e, xhr, options) {
        xhr.setRequestHeader(header, token);
        xhr.setRequestHeader("Content-Type", "application/json");
    });

	//Hide Loading Box (Preloader)
	function handlePreloader() {
		if($('.preloader').length){
			$('.preloader').delay(200).fadeOut(500);
		}
	}

	//Update Header Style and Scroll to Top
	function headerStyle() {
		if($('.main-header').length){
			var windowpos = $(window).scrollTop();
			var siteHeader = $('.main-header');
			var scrollLink = $('.scroll-to-top');
			if (windowpos >= 1) {
				siteHeader.addClass('fixed-header');
				scrollLink.fadeIn(300);
			} else {
				siteHeader.removeClass('fixed-header');
				scrollLink.fadeOut(300);
			}
		}
	}
	
	headerStyle();

	$("#registration-button").click(function() {
	    var regForm = new Object();
        regForm.username = $("#registration-form input[name='username']").val();
        regForm.email = $("#registration-form input[name='email']").val();
        regForm.password = $("#registration-form input[name='password']").val();
        regForm.password2 = $("#registration-form input[name='password2']").val();
        regForm.country = $("#registration-form input[name='country']").val();

        $.post('/registration', JSON.stringify(regForm), function(response) {
            if(response.status === "OK") {
                 $(".sec-title:last h2").text("Thank you! Check your e-mail to continue.");
                 $("form:last").hide();
            } else {
                 $("#registration-form input").removeAttr("class");
                 $("#registration-form input[name='"+response.element+"']").addClass("invalid").focus();
            }
        }, 'json');
	});

    $("#accept-button").click(function() {
        var regForm = new Object();
        regForm.username = $("#registration-form input[name='username']").val();
        regForm.password = $("#registration-form input[name='password']").val();
        regForm.password2 = $("#registration-form input[name='password2']").val();
        regForm.country = $("#registration-form input[name='country']").val();

        $.post('/accept-invite/' + inviteID + '/' + key, JSON.stringify(regForm), function(response) {
            if(response.status === "OK") {
                 $("#login-form #login-username").val(response.element);
                 $("#login-form #login-password").val($("#registration-form input[name='password']").val());
                 $("#login-submit").click();
            } else {
                 $("#registration-form input").removeAttr("class");
                 $("#registration-form input[name='"+response.element+"']").addClass("invalid").focus();
            }
        }, 'json');
    });

	//Submenu Dropdown Toggle
	if($('.main-header li.dropdown ul').length){
		$('.main-header .navigation li.dropdown').append('<div class="dropdown-btn"><span class="fa fa-angle-down"></span></div>');

		//Dropdown Button
		$('.main-header li.dropdown .dropdown-btn').on('click', function() {
			$(this).prev('ul').slideToggle(500);
		});
	}

	
	//Add One Page nav
	if($('.scroll-nav').length) {
		$('.scroll-nav ul').onePageNav();
	}
	
	//Hide Bootstrap Menu On Click over Mobile View
	$('.scroll-nav ul.navigation > li > a').on('click', function(){
		var windowWidth = $(window).width();
		if (windowWidth <= 767) {
			$('.nav-outer .navbar-toggle').trigger( "click" );
		}
	});
	
	
	//Single Item Carousel
	if ($('.single-item-carousel').length) {
		$('.single-item-carousel').owlCarousel({
			loop:true,
			margin:0,
			items: 1,
			nav:true,
			smartSpeed: 700,
			autoplay: true,
			navText: [ '<span class="fa fa-angle-left"></span>', '<span class="fa fa-angle-right"></span>' ],
			
		});
	}
	
	
	//Inventory Tabs
	if($('.inventory-tabs').length){
		$('.inventory-tabs .tab-buttons .tab-btn').on('click', function(e) {
			e.preventDefault();
			var target = $($(this).attr('data-tab'));
			
			if ($(target).hasClass('active-tab')){
				return false;
			}else{
				target.parents('.inventory-tabs').find('.tab-buttons').find('.tab-btn').removeClass('active-btn');
				$(this).addClass('active-btn');
				target.parents('.inventory-tabs').find('.tabs-content').find('.tab').removeClass('active-tab');
				$(target).addClass('active-tab');
			}
		});
	}
	
	
	//Default Tabs Box
	if($('.tabs-box').length){
		$('.tabs-box .tab-buttons .tab-btn').on('click', function(e) {
			e.preventDefault();
			var target = $($(this).attr('data-tab'));
			
			if ($(target).is(':visible')){
				return false;
			}else{
				target.parents('.tabs-box').find('.tab-buttons').find('.tab-btn').removeClass('active-btn');
				$(this).addClass('active-btn');
				target.parents('.tabs-box').find('.tabs-content').find('.tab').fadeOut(0);
				target.parents('.tabs-box').find('.tabs-content').find('.tab').removeClass('active-tab');
				$(target).fadeIn(300);
				$(target).addClass('active-tab');
			}
		});
	}
	
	
	//Sponsors Slider One
	if ($('.sponsors-slider-one').length) {
		$('.sponsors-slider-one').owlCarousel({
			loop:true,
			margin:10,
			nav:true,
			smartSpeed: 500,
			autoplay: true,
			navText: [ '<span class="fa fa-angle-left"></span>', '<span class="fa fa-angle-right"></span>' ],
			responsive:{
				0:{
					items:1
				},
				600:{
					items:2
				},
				800:{
					items:3
				},
				1024:{
					items:4
				},
				1200:{
					items:4
				}
			}
		});    		
	}
	
	
	//Clients Carousel
	if ($('.clients-carousel').length) {
		$('.clients-carousel').owlCarousel({
			loop:true,
			margin:30,
			nav:true,
			smartSpeed: 500,
			autoplay: true,
			navText: [ '<span class="fa fa-angle-left"></span>', '<span class="fa fa-angle-right"></span>' ],
			responsive:{
				0:{
					items:1
				},
				600:{
					items:1
				},
				800:{
					items:1
				},
				1024:{
					items:2
				}
			}
		});    		
	}
	
	
	//Testimonials Carousel One
	if ($('.testimonials-carousel-one').length) {
		$('.testimonials-carousel-one').owlCarousel({
			loop:true,
			margin:30,
			items: 1,
			nav:true,
			smartSpeed: 700,
			autoplay: true,
			navText: [ '<span class="fa fa-angle-left"></span>', '<span class="fa fa-angle-right"></span>' ],
			
		});
	}
	
	
	//Testimonials Style Two
	if ($('.testimonial-style-two').length) {
		$('.testimonial-style-two').owlCarousel({
			loop:true,
			margin:10,
			nav:true,
			smartSpeed: 500,
			autoplay: true,
			navText: [ '<span class="fa fa-angle-left"></span>', '<span class="fa fa-angle-right"></span>' ],
			responsive:{
				0:{
					items:1
				},
				600:{
					items:1
				},
				800:{
					items:2
				},
				1024:{
					items:2
				},
				1200:{
					items:3
				}
			}
		});    		
	}
	
	
	//LightBox / Fancybox
	if($('.lightbox-image').length) {
		$('.lightbox-image').fancybox({
			openEffect  : 'fade',
			closeEffect : 'fade',
			helpers : {
				media : {}
			}
		});
	}
	

	// Scroll to a Specific Div
	if($('.scroll-to-target').length){
		$(".scroll-to-target").on('click', function() {
			var target = $(this).attr('data-target');
		   // animate
		   $('html, body').animate({
			   scrollTop: $(target).offset().top
			 }, 1000);

		});
	}


	// Elements Animation
	if($('.wow').length){
		var wow = new WOW(
		  {
			boxClass:     'wow',      // animated element css class (default is wow)
			animateClass: 'animated', // animation css class (default is animated)
			offset:       0,          // distance to the element when triggering the animation (default is 0)
			mobile:       true,       // trigger animations on mobile devices (default is true)
			live:         true       // act on asynchronously loaded content (default is true)
		  }
		);
		wow.init();
	}


/* ==========================================================================
   When document is Scrollig, do
   ========================================================================== */

	$(window).on('scroll', function() {
		headerStyle();
	});
	

/* ==========================================================================
   When document is loading, do
   ========================================================================== */

	$(window).on('load', function() {
		handlePreloader();
	});

})(window.jQuery);