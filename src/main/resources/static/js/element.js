"use strict";
$.noConflict();
var $ = jQuery;

$(document).ready(function($) {

    /**
     *  Smooth Scroll
     *  -----------------------------------------------------------------------
     *  For All the links smoothly animating to their respective section
     */
    $('.scrollbtn a[href*="#"]:not([href="#"])').on("click", function() {
        if (location.pathname.replace(/^\//, '') == this.pathname.replace(/^\//, '') && location.hostname == this.hostname) {
            var target = $(this.hash);
            target = target.length ? target : $('[name=' + this.hash.slice(1) + ']');
            if (target.length) {
                $('html, body').animate({
                    scrollTop: target.offset().top
                }, 1000);
                return false;
            }
        }
    });


    /**
     *  Active Menu Js
     *  -----------------------------------------------------------------------
     *  For Navbar Section adding active to selected menu
     */
    $('.navbar-nav li a').on("click", function(e) {
        $('.navbar-nav li').removeClass('active');
        var $parent = $(this).parent();
        if (!$parent.hasClass('active')) {
            $parent.addClass('active');
        }
    });


    /**
     *  Navbar Close Icon
     *  -----------------------------------------------------------------------
     *  For Navbar Section Close Icon
     */
    $(".navbar-toggle").on("click", function() {
        $(this).toggleClass("active");
        $("#header").toggleClass("headClr");
        $("body").toggleClass("popup-open");
    });

    $('.main-menu ul li a').click(function() {
        $("body").removeClass("popup-open");
        $(".navbar-collapse").removeClass('in');
    });


    /**
     *  Navbar Responsive Js
     *  -----------------------------------------------------------------------
     *  For Navbar Section Responsive Js
     */
    function resMenu() {
        if ($(window).width() < 1200) {
            $('.main-menu ul li a').on("click", function() {
                $(".navbar-collapse").removeClass("in");
                $(".navbar-toggle").addClass("collapsed").removeClass("active");
                $("#header").removeClass("headClr");
            });
        }
    }
    resMenu();


    /**
     *  Back To Top
     *  -----------------------------------------------------------------------
     *  For Back To Top button Js
     */
    var offset = 300,
        offset_opacity = 1200,
        scroll_top_duration = 700,
        $back_to_top = $('.back-to-top');

    $(window).scroll(function() {
        ($(this).scrollTop() > offset) ? $back_to_top.addClass('cd-is-visible'): $back_to_top.removeClass('cd-is-visible cd-fade-out');
        if ($(this).scrollTop() > offset_opacity) {
            $back_to_top.addClass('cd-fade-out');
        }

        // On scroll header reduce js  
        var scroll = $(window).scrollTop();
        if (scroll >= 100) {
            $("#header").addClass("fixed");
        } else {
            $("#header").removeClass("fixed");
        }

        if ($(window).width() < 767) {
            $("#header").removeClass("fixed");
        }

    });

    $back_to_top.on('click', function(event) {
        event.preventDefault();
        $('body,html').animate({
            scrollTop: 0,
        }, scroll_top_duration);
    });


    /**
     *  Parallax
     *  -----------------------------------------------------------------------
     *  For All the sections with parallax background images
     */
    $('.parallax-fix').each(function () {
        if ($(this).children('.parallax-background-img').length) {
            var imgSrc = jQuery(this).children('.parallax-background-img').attr('src');
            jQuery(this).css('background', 'url("' + imgSrc + '")');
            jQuery(this).children('.parallax-background-img').remove();
            $(this).css('background-position', '50% 0%');
        }

    });
    var IsParallaxGenerated = false;
    function SetParallax() {
        if ($(window).width() > 1030 && !IsParallaxGenerated) {
            $('.parallaximg').parallax("50%", 0.05);
            $('.parallaximg-2').parallax("50%", 0.01);
            $('.parallaximg-3').parallax("50%", 0.01);
            $('.parallaximg-4').parallax("50%", 0);
            $('.parallaximg-5').parallax("50%", 0);
            $('.parallaximg-6').parallax("50%", 0);
            IsParallaxGenerated = true;
        }
    }

    SetParallax();


    /**
     *  Full Screen Width/Height Js
     *  -----------------------------------------------------------------------
     *  For Header Banner Section Full screen height/width
     */
    function SetResizeContent() {
        var minheight = $(window).height();
        $(".full-screen-mode").css('min-height', minheight);
        
        var minwidth = $(window).width();
        $(".full-screen-width-mode").css('min-width', minwidth);
    }

    SetResizeContent();


    /**
     *  Owl Carousel
     *  -----------------------------------------------------------------------
     *  For Today's Special Menu Section Slider
     */
    $("#owl-breakfast").owlCarousel({
        items: 3,
        loop: true,
        mouseDrag: true,
        nav: true,
        margin:19,
        dots: false,
        responsive: {
            1200: {
                items: 3
            },
            1000: {
                items: 3
            },
            600: {
                items: 2
            },
            200: {
                items: 1
            }
        }

    });

    $("#owl-lunch").owlCarousel({
        items: 3,
        loop: true,
        mouseDrag: true,
        nav: true,
        margin:19,
        dots: false,
        responsive: {
            1200: {
                items: 3
            },
            1000: {
                items: 3
            },
            600: {
                items: 2
            },
            200: {
                items: 1
            }
        }

    });

    $("#owl-dinner").owlCarousel({
        items: 3,
        loop: true,
        mouseDrag: true,
        nav: true,
        margin:19,
        dots: false,
        responsive: {
            1200: {
                items: 3
            },
            1000: {
                items: 3
            },
            600: {
                items: 2
            },
            200: {
                items: 1
            }
        }

    });


    /**
     *  Video JS
     *  -----------------------------------------------------------------------
     *  For Video Section Video Plays on clicking play icon JS
     */
    $('#recipe-video').css("display", "none");
    $(".video-play-icon").on('click', function() {
        var video = $("#recipe-video").get(0);
        video.play();
        $('#recipe-video').css("display", "block");
        $(this).css("display", "none");
        $('.video-menu-block h2').css("display", "none");
        return false;
    });

    // wow animation Js
    new WOW().init();


    /**
     *  Equal Height 
     *  -----------------------------------------------------------------------
     *  For Team Section Equal height of divs
     */
    $(".test-video-box").height($(".team-text").height());   


    /**
     *  Video JS
     *  -----------------------------------------------------------------------
     *  For Team Section Video Plays on clicking play icon JS
     */
    $(".team-video").css("display","none");
    $(window).on("load",  function(){
        $("#owl-team .owl-item").each(function() {
          if($(this).hasClass("active")){
            $(".test-video-play-icon").on('click', function() {
                $('video').each(function() {
                    $(this).get(0).play();
                }); 
                $(this).css("display","none");
                $("#owl-team .owl-item.active .team-video-block figure").css("display","none");
                $("#owl-team .owl-item.active .team-video").css("display","block");
                return false;
            });
          }
        });
    });   


    /**
     *  Owl Carousel
     *  -----------------------------------------------------------------------
     *  For Team Section Slider
     */
    $("#owl-team").owlCarousel({
        navigation: true,
        items:1,
        merge:true,
        loop:true,
        video:true,
        lazyLoad:true,
        center:true,
        itemsDesktop: false,
        itemsDesktopSmall: false,
        itemsTablet: false,
        itemsMobile: false,
        pagination: false
    });


    /**
     *  Call Masonry.
     *  -----------------------------------------------------------------------
     *  For Food Gallery Section Masonry Grid
     */
    $('.food-gallery').imagesLoaded( function(){
        $('.food-gallery').masonry({

            columnWidth     : '.food-gallery-item',
            itemSelector    : '.food-gallery-item',
            gutter          : 0
    
        });
    });


    /**
     *  Magnific popup.
     *  -----------------------------------------------------------------------
     *  For Food Gallery Section Popup
     */
    $('#popup-gallery').magnificPopup({
          delegate: 'a',
          type: 'image',
          tLoading: 'Loading image #%curr%...',
          mainClass: 'mfp-img-mobile',
          gallery: {
            enabled: true,
            navigateByImgClick: true,
            preload: [0,1] // Will preload 0 - before current, and 1 after the current image
          },
          image: {
            tError: '<a href="%url%">The image #%curr%</a> could not be loaded.',
            /*titleSrc: function(item) {
              return item.el.attr('title') + '<small>by Abc</small>';
            }*/
          }
        });


    /**
     *  Owl Carousel
     *  -----------------------------------------------------------------------
     *  For Testimonial Section Slider
     */
    $("#owl-testimonial").owlCarousel({
        navigation: true,
        items:1,
        merge:true,
        loop:true,
        video:true,
        lazyLoad:true,
        center:true,
        itemsDesktop: false,
        itemsDesktopSmall: false,
        itemsTablet: false,
        itemsMobile: false,
        pagination: false
    });


    /**
     *  Date/Time Picker
     *  -----------------------------------------------------------------------
     *  For Reservation Form Section date & time picker
     */
    $('#date-picker').datetimepicker({
        format:'DD.MM.YYYY'
    });
    $('#time-picker').datetimepicker({
        format: 'LT'
    });



    $('.team-item').simpleequalheight({
        responsive: true
    });

});


;(function($, window, document, undefined) {

    var pluginName = 'simpleequalheight',
        defaults = {
            responsive: true
        };

    function Plugin(elements, options) {
        this.elements = elements;

        this.options = $.extend({}, defaults, options);

        this._defaults = defaults;
        this._name = pluginName;

        this.active = false;

        this.init();
    }

    Plugin.prototype = {

        init: function() {
            if (!this.options.wait) {
                this.start();
            }

            if (this.options.responsive) {
                $(window).on('resize', $.proxy(this.onWindowResize, this));
            }
        },

        /**
         * Set the height of each element to the height of the highest element.
         */
        magic: function() {
            // Store the height of the highest element.
            var topHeight = 0;

            // Reset min-height and height. Otherwise we will run in trouble if
            // we execute this function a second time (on resize for instance).
            this.reset();
            var maxHeight = -1;
            $('.equalheight').each(function() {
                maxHeight = maxHeight > $(this).height() ? maxHeight : $(this).height();
            });
            $('.equalheight').each(function() {
                $(this).height(maxHeight);
            });

            // If this.stop was called during execution of this function, abort
            if (!this.active) return;

            // Set min-height on elements.
            for (var j = 0; j < this.elements.length; j++) {
                var element = $(this.elements[j]);

                // Height behaves like min-height when display is table-cell.
                if (element.css("display") === "table-cell") {
                    element.css("height", topHeight);
                } else {
                    element.css("min-height", topHeight);
                }
            }

            // If this.stop was called during executing of this function, reset
            if (!this.active) this.reset();
        },

        /**
         * Remove all styles added by this plugin.
         */
        reset: function() {
            this.elements.css("min-height", "");
            this.elements.css("height", "");
        },

        /**
         * Start the plugin.
         */
        start: function() {
            this.active = true;
            this.magic();
        },

        /**
         * Stop the plugin and reset the elements.
         */
        stop: function() {
            this.active = false;
            this.reset();
        },

        /**
         * Call the magic method on window resize, if allowed.
         */
        onWindowResize: function() {
            if (this.active) {
                this.magic();
            }
        }

    };

    // Release the beast
    $.fn[pluginName] = function(options) {
        return new Plugin(this, options);
    };

})(jQuery, window, document);