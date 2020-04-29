jQuery( document ).ready( function() {
  jQuery( '.additional-facepile' ).hide();

  jQuery( '.toggle-additional-facepiles' ).click( function() {
    jQuery( this ).parent( 'ul' ).find( '.additional-facepile' ).toggle();
  } );
} );
