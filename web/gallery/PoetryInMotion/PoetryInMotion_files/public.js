var runPYS = function () {

    var $ = window.jQuery;

    if (pys_fb_pixel_options.run === true) {
        return; // prevent double firing from Cookiebot.onaccept
    } else {
        pys_fb_pixel_options.run = true;
    }

    // load FB pixel
    !function (f, b, e, v, n, t, s) {
        if (f.fbq) return;
        n = f.fbq = function () {
            n.callMethod ?
                n.callMethod.apply(n, arguments) : n.queue.push(arguments)
        };
        if (!f._fbq) f._fbq = n;
        n.push = n;
        n.loaded = !0;
        n.version = '2.0';
        n.agent = 'dvpixelyoursite';
        n.queue = [];
        t = b.createElement(e);
        t.async = !0;
        t.src = v;
        s = b.getElementsByTagName(e)[0];
        s.parentNode.insertBefore(t, s)
    }(window,
        document, 'script', 'https://connect.facebook.net/en_US/fbevents.js');

    /**
     * WooCommerce AddToCart on button
     */
    if (pys_fb_pixel_options.woo.addtocart_enabled) {

        window.pys_woo_product_data = window.pys_woo_product_data || [];

        // Loop, any kind of "simple" product, except external
        $('.add_to_cart_button:not(.product_type_variable)').click(function (e) {

            var product_id = $(this).data('product_id');

            if (typeof product_id !== 'undefined') {
                if (typeof pys_woo_product_data[product_id] !== 'undefined') {
                    fbq('track', 'AddToCart', pys_woo_product_data[product_id]);
                }
            }

        });

        // Single Product
        $('.single_add_to_cart_button').click(function (e) {

            var $button = $(this),
                $form = $button.closest('form'),
                is_variable = false,
                qty,
                product_id = pys_fb_pixel_options.woo.product_id;

            if ($button.hasClass('disabled')) {
                return;
            }

            if ($form.length === 0) {
                return; // is external product, not supported on Free
            }

            if ($form.hasClass('variations_form')) {
                is_variable = true;
            }

            if (is_variable) {

                qty = parseInt($form.find('input[name="quantity"]').val());

                if (pys_fb_pixel_options.woo.product_data !== 'main') {
                    product_id = parseInt($form.find('input[name="variation_id"]').val());
                }

            } else {

                qty = parseInt($form.find('input[name="quantity"]').val());

            }

            if (typeof pys_woo_product_data[product_id] !== 'undefined') {

                var params = pys_woo_product_data[product_id];

                // maybe customize value option
                if (pys_fb_pixel_options.woo.product_value_enabled && pys_fb_pixel_options.woo.product_value_option !== 'global') {
                    params.value = params.value * qty;
                }

                // update contents qty param
                params.contents[0].quantity = qty;

                fbq('track', 'AddToCart', params);

            }

        });

    }

    regularEvents();
    customCodeEvents();

    // EDD AddToCart
    $('.edd-add-to-cart').click(function () {

        try {

            // extract pixel event ids from classes like 'pys-event-id-{UNIQUE ID}'
            var classes = $.grep(this.className.split(" "), function (element, index) {
                return element.indexOf('pys-event-id-') === 0;
            });

            // verify that we have at least one matching class
            if (typeof classes == 'undefined' || classes.length == 0) {
                return;
            }

            // extract event id from class name
            var regexp = /pys-event-id-(.*)/;
            var event_id = regexp.exec(classes[0]);

            if (event_id == null) {
                return;
            }

            evaluateEventByID(event_id[1], pys_edd_ajax_events);

        } catch (e) {
            console.log(e);
        }

    });

    /**
     * Process Init, General, Search, Standard (except custom code), WooCommerce (except AJAX AddToCart, Affiliate and
     * PayPal events. In case if delay param is present - event will be fired after desired timeout.
     */
    function regularEvents() {

        if (typeof pys_events == 'undefined') {
            return;
        }

        for (var i = 0; i < pys_events.length; i++) {

            var eventData = pys_events[i];

            if (eventData.hasOwnProperty('delay') == false || eventData.delay == 0) {

                fbq(eventData.type, eventData.name, eventData.params);

            } else {

                setTimeout(function (type, name, params) {
                    fbq(type, name, params);
                }, eventData.delay * 1000, eventData.type, eventData.name, eventData.params);

            }

        }

    }

    /**
     * Process only custom code Standard events.
     */
    function customCodeEvents() {

        if (typeof pys_customEvents == 'undefined') {
            return;
        }

        $.each(pys_customEvents, function (index, code) {
            eval(code);
        });

    }

    /**
     * Fire event with {eventID} from =events= events list. In case of event data have =custom= property, code will be
     * evaluated without regular Facebook pixel call.
     */
    function evaluateEventByID(eventID, events) {

        if (typeof events == 'undefined' || events.length == 0) {
            return;
        }

        // try to find required event
        if (events.hasOwnProperty(eventID) == false) {
            return;
        }

        var eventData = events[eventID];

        if (eventData.hasOwnProperty('custom')) {

            eval(eventData.custom);

        } else {

            fbq(eventData.type, eventData.name, eventData.params);

        }

    }

};

jQuery(document).ready(function ($) {

    if (typeof pys_fb_pixel_options !== 'undefined') {

        // tracking in not disabled with filter by 3rd party
        if (!pys_fb_pixel_options.gdpr.disable) {

            var getCookie = function (name) {
                var value = "; " + document.cookie;
                var parts = value.split("; " + name + "=");
                if (parts.length === 2) return parts.pop().split(";").shift();
            };

            /**
             * @link http://gdpr-wp.com/knowledge-base/functions-shortcodes/
             * @link https://www.cookiebot.com/en/developer/
             *
             * "enable_before_consent" option logic:
             * - when it ON, run tracking with no cookie or consent is given
             * - when it OFF, run tracking only when consent is given
             */

            /*
            if (pys_fb_pixel_options.gdpr.gdpr_enabled && typeof has_consent === 'function') {

                if (has_consent('marketing')) {
                    runPYS();
                } else {
                    console.warn('PixelYourSite is disabled by GDPR plugin.');
                }

            } else
            */
            if (pys_fb_pixel_options.gdpr.ginger_enabled) {

                var ginger_cookie = getCookie('ginger-cookie');

                if (pys_fb_pixel_options.gdpr.enable_before_consent) {
                    if (typeof ginger_cookie === 'undefined' || ginger_cookie === 'Y') {
                        runPYS();
                    } else {
                        console.warn('PixelYourSite is disabled by Ginger - EU Cookie Law plugin.');
                    }
                } else {
                    if (ginger_cookie === 'Y') {
                        runPYS();
                    } else {
                        console.warn('PixelYourSite is disabled by Ginger - EU Cookie Law plugin.');
                    }
                }

            } else if (pys_fb_pixel_options.gdpr.cookiebot_enabled && typeof Cookiebot !== 'undefined') {

                if (pys_fb_pixel_options.gdpr.enable_before_consent) {
                    if (Cookiebot.consented === false || Cookiebot.consent.marketing) {
                        runPYS();
                    } else {
                        console.warn('PixelYourSite is disabled by Cookiebot plugin.');
                    }
                } else {
                    if (Cookiebot.consent.marketing) {
                        runPYS();
                    } else {
                        console.warn('PixelYourSite is disabled by Cookiebot plugin.');
                    }
                }

                // setup Cookiebot on accept callback
                Cookiebot.onaccept = function () {
                    if (Cookiebot.consent.marketing) {
                        runPYS();
                    }
                }

            } else {
                runPYS();
            }

        } else {
            console.warn('PixelYourSite is disabled by GDPR.');
        }

    }

});