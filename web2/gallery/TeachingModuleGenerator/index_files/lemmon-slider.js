/*
 * Lemmon Slider - jQuery Plugin
 * Simple and lightweight slider/carousel supporting variable elements/images widths.
 *
 * Examples and documentation at: http://jquery.lemmonjuice.com/plugins/slider-variable-widths.php
 *
 * Copyright (c) 2011 Jakub PelÃ¡k <jpelak@gmail.com>
 *
 * Version: 0.2 (9/6/2011)
 * Requires: jQuery v1.4+
 *
 * Licensed under the MIT license:
 *   http://www.opensource.org/licenses/mit-license.php
 */
(function( $ ) {

    var _css = {};

    var methods = {
        //
        // Initialzie plugin
        //
        init:    function( options ) {
            options = $.extend( {}, $.fn.lemmonSlider.defaults, options );

            return this.each( function() {

                var $slider = $( this ),
                    data = $slider.data( 'slider' );

                if( !data ) {

                    var $sliderContainer = $slider.find( options.slider ),
                        $sliderControls = $slider.next().filter( '.controls' ),
                        $items = $sliderContainer.find( options.items ),
                        originalWidth = 1;

                    $items.each( function() { originalWidth += $( this ).outerWidth( true ) } );
                    $sliderContainer.width( originalWidth );

                    // slide to last item
                    if( options.slideToLast ) $sliderContainer.css( 'padding-right', $slider.width() );

                    // infinite carousel
                    if( options.infinite ) {
                        $slider.data( 'slider-infinite', true );

                        originalWidth = originalWidth * 3;
                        $sliderContainer.width( originalWidth );

                        $items.clone(true, true).addClass( '-after' ).insertAfter( $items.filter( ':last' ) );
                        $items.filter( ':first' ).before( $items.clone(true, true).addClass( '-before' ) );

                        $items = $sliderContainer.find( options.items );

                    }

                    $slider.items = $items;
                    $slider.options = options;

                    // first item
                    //$items.filter( ':first' ).addClass( 'active' );

                    slideTo( {}, $slider, 0, 0, 0 );

                    // attach events
                    $slider.bind( 'nextSlide', function( e, t ) {

                        var scroll = $slider.scrollLeft();
                        var x = 0;
                        var slide = 0;

                        $items.each( function( i ) {
                            if( x == 0 && $( this ).position().left > options.offset ) {
                                x = $( this ).position().left;
                                slide = i;
                            }
                        } );

                        if( x > 0 && $sliderContainer.outerWidth() - scroll - $slider.width() - 1 > 0 ) {
                            slideTo( e, $slider, scroll + x, slide, 'slow' );
                        } else if( options.loop ) {
                            // return to first
                            slideTo( e, $slider, 0, 0, 'slow' );
                        }

                    } );
                    $slider.bind( 'prevSlide', function( e, t ) {

                        var scroll = $slider.scrollLeft();
                        var x = 0;
                        var slide = 0;

                        $items.each( function( i ) {
                            if( $( this ).position().left < options.offset ) {
                                x = $( this ).position().left;
                                slide = i;
                            }
                        } );

                        if( x ) {
                            slideTo( e, $slider, scroll + x, slide, 'slow' )
                        } else if( options.loop ) {
                            // return to last
                            var a = $sliderContainer.outerWidth() - $slider.width();
                            var b = $items.filter( ':last' ).position().left;
                            slide = $items.size() - 1;
                            if( a > b ) {
                                slideTo( e, $slider, b, slide, 'slow' );
                            } else {
                                slideTo( e, $slider, a, slide, 'slow' );
                            }
                        }

                    } );
                    $slider.bind( 'nextPage', function( e, t ) {

                        var scroll = $slider.scrollLeft();
                        var w = $slider.width();
                        var x = 0;
                        var slide = 0;

                        $items.each( function( i ) {
                            if( $( this ).position().left < w ) {
                                x = $( this ).position().left;
                                slide = i;
                            }
                        } );

                        if( x > 0 && scroll + w + 1 < originalWidth ) {
                            slideTo( e, $slider, scroll + x, slide, 'slow' );
                        } else if( options.loop ) {
                            // return to first
                            slideTo( e, $slider, 0, 0, 'slow' );
                        }

                    } );
                    $slider.bind( 'prevPage', function( e, t ) {

                        var scroll = $slider.scrollLeft();
                        var w = $slider.width();
                        var x = 0;

                        $items.each( function( i ) {
                            if( $( this ).position().left < 1 - w ) {
                                x = $( this ).next().position().left;
                                slide = i;
                            }
                        } );

                        if( scroll ) {
                            if( x == 0 ) {
                                //$slider.animate({ 'scrollLeft' : 0 }, 'slow' );
                                slideTo( e, $slider, 0, 0, 'slow' );
                            } else {
                                //$slider.animate({ 'scrollLeft' : scroll + x }, 'slow' );
                                slideTo( e, $slider, scroll + x, slide, 'slow' );
                            }
                        } else if( options.loop ) {
                            // return to last
                            var a = $sliderContainer.outerWidth() - $slider.width();
                            var b = $items.filter( ':last' ).position().left;
                            if( a > b ) {
                                $slider.animate( { 'scrollLeft': b }, 'slow' );
                            } else {
                                $slider.animate( { 'scrollLeft': a }, 'slow' );
                            }
                        }

                    } );
                    $slider.bind( 'slideTo', function( e, i, t ) {

                        slideTo(
                            e, $slider,
                            $slider.scrollLeft() + $items.filter( ':eq(' + i + ')' ).position().left,
                            i, t );

                    } );

                    // controls
                    $sliderControls.find( '.next-slide' ).on('click', function() {
                        $slider.trigger( 'nextSlide' );
                        return false;
                    } );
                    $sliderControls.find( '.prev-slide' ).on('click', function() {
                        $slider.trigger( 'prevSlide' );
                        return false;
                    } );
                    $sliderControls.find( '.next-page' ).on('click', function() {
                        $slider.trigger( 'nextPage' );
                        return false;
                    } );
                    $sliderControls.find( '.prev-page' ).on('click', function() {
                        $slider.trigger( 'prevPage' );
                        return false;
                    } );

                    //if ( typeof $slider.options.create == 'function' ) $slider.options.create();

                    $slider.data( 'slider', {
                        'target':  $slider,
                        'options': options
                    } )

                }

            } );

        },
        //
        // Add Item
        //
        addItem: function( options ) {
            var options = $.extend( {}, $.fn.lemmonSlider.defaults, options );

            var $slider = $( this ),
                $sliderContainer = $slider.find( options.slider ),
                $sliderControls = $slider.next().filter( '.controls' ),
                $items = $sliderContainer.find( options.items )

            options.infinite = $slider.data( 'slider-infinite' )

            if( !options.item ) {
                return false
            }
            methods.destroy.apply( this );
            if( options.prepend ) {
                $sliderContainer.prepend( options.item );
            } else {
                $sliderContainer.append( options.item );
            }
            methods.init.apply( this, [options] );
        },
        //
        // Destroy plugin
        //
        destroy: function() {

            return this.each( function() {

                var $slider = $( this ),
                    $sliderControls = $slider.next().filter( '.controls' ),
                    $items = $slider.find( '> *:first > *' ),
                    data = $slider.data( 'slider' );

                $slider.unbind( 'nextSlide' );
                $slider.unbind( 'prevSlide' );
                $slider.unbind( 'nextPage' );
                $slider.unbind( 'prevPage' );
                $slider.unbind( 'slideTo' );

                $sliderControls.find( '.next-slide' ).unbind( 'click' );
                $sliderControls.find( '.prev-slide' ).unbind( 'click' );
                $sliderControls.find( '.next-page' ).unbind( 'click' );
                $sliderControls.find( '.next-page' ).unbind( 'click' );

                $slider.removeData( 'slider' );

                if( $slider.data( 'slider-infinite' ) ) {
                    $.merge( $items.filter( '.-before' ), $items.filter( '.-after' ) ).each( function( index, item ) {
                        $( item ).remove();
                    } );
                }
            } );

        }
        //
        //
        //
    }
    //
    // Private functions
    //
    function slideTo( e, $slider, x, i, t ) {

        $slider.items.filter( 'li:eq(' + i + ')' ).addClass( 'active' ).siblings( '.active' ).removeClass( 'active' );

        if( $slider.options.center ) {
            var currentElement = $( $slider.items[ i ] );
            $slider.options.offset = Math.floor( ( $('.qode_image_gallery_holder').width() - currentElement.width() ) / 2 );
            console.log( 'set offset to ', $slider.options.offset );
        }

        if( typeof t == 'undefined' ) {
            t = 'slow';
        }
        if( t ) {
            $slider.animate( { 'scrollLeft': x - $slider.options.offset }, t, function() {
                checkInfinite( $slider );
            } );
        } else {
            var time = 0;
            $slider.scrollLeft( x - $slider.options.offset );
            checkInfinite( $slider );
        }

        //if ( typeof $slider.options.slide == 'function' ) $slider.options.slide( e, i, time );
    }

    function checkInfinite( $slider ) {

        var $active = $slider.items.filter( '.active' );
        if( $active.hasClass( '-before' ) ) {

            var i = $active.prevAll().size();
            $active.removeClass( 'active' );
            $active = $slider.items.filter( ':not(.-before):eq(' + i + ')' ).addClass( 'active' );
            $slider.scrollLeft( $slider.scrollLeft() + $active.position().left - $slider.options.offset );

        } else if( $active.hasClass( '-after' ) ) {

            var i = $active.prevAll( '.-after' ).size();
            $active.removeClass( 'active' );
            $active = $slider.items.filter( ':not(.-before):eq(' + i + ')' ).addClass( 'active' );
            $slider.scrollLeft( $slider.scrollLeft() + $active.position().left - $slider.options.offset );

        }

    }

    //
    // Debug
    //
    function debug( text ) {
        $( '#debug span' ).text( text );
    }

    //
    //
    //
    $.fn.lemmonSlider = function( method, options ) {
        if( methods[method] ) {
            return methods[ method ].apply( this, Array.prototype.slice.call( arguments, 1 ) );
        } else if( typeof method === 'object' || !method ) {
            return methods.init.apply( this, arguments );
        } else {
            $.error( 'Method ' + method + ' does not exist on jQuery.lemmonSlider' );
        }

    };
    //
    //
    //
    $.fn.lemmonSlider.defaults = {

        'items':       '> *',
        'loop':        true,
        'slideToLast': false,
        'slider':      '> *:first',
        // since 0.2
        'infinite':    true,
        'center':      true,
        'offset':      0

    }

})( jQuery );
