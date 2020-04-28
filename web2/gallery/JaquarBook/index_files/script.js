/**
 * Create a select list from the menu
 * Credits: http://bit.ly/ultoselect
 */
(function($) {
  $(function() {
    if (!$('body').hasClass('advanced-menu')) {
      return false;
    }

    var $select = $('<select>')
      .appendTo('#nav');

    var $option = $('<option>')
      .text(objectL10n.blaskan_navigation_title)
      .val('')                       
      .appendTo($select);

    $('#nav li').each(function() {
      var $li    = $(this),
          $a     = $li.find('> a'),
          $p     = $li.parents('li'),
          prefix = new Array($p.length + 1).join('-');

      var $option = $('<option>')
          .text(prefix + ' ' + $a.text())
          .val($a.attr('href'))                       
          .appendTo($select);

      if ($li.hasClass('current_page_item')) {
        $option.attr('selected', 'selected');
      }

      if ($li.children().size() > 1) {
        $li.addClass('has-children');
      }
    });

    $('#nav select').change(function() {
      var location = $(this).find('option:selected').val();

      if (location.length != 0) {
        window.location = location;
      }
    });

    $('#content').fitVids();
  });
})(jQuery);