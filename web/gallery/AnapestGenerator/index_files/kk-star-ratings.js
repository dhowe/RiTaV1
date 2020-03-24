jQuery(document).ready(function ($) {
    $('.kk-star-ratings').each(function (i, el) {
        var $el = $(el);
        var $activeStars = $('.kksr-active-stars', $el);
        var $legend = $('.kksr-legend', $el);
        var $score = $('.kksr-legend-score', $legend);
        var $count = $('.kksr-legend-meta', $legend);

        var busy = false;

        $el.on('click', function (e) {
            if (busy || $el.hasClass('kksr-disable')) {
                return;
            }

            busy = true;

            var id = $el.data('id');

            if (! id) {
                return busy = false;
            }

            var star = e.target;
            var $star = $(star);
            var rating = $star.data('star');

            if (! rating) {
                return busy = false;
            }

            $.ajax({
                type: 'POST',
                url: kk_star_ratings.endpoint,
                data: {
                    id: id,
                    rating: rating,
                    action: 'kk-star-ratings',
                    nonce: kk_star_ratings.nonce
                },
                dataType: 'json',
                success: function (response) {
                    $activeStars.width(response.width + 'px');
                    $score.text(response.score);
                    $count.text(response.count);
                    $legend.show();
                    if (response.disable) {
                        $el.addClass('kksr-disable');
                    }
                    console.log('success', response);
                },
                error: function (xhr) {
                    console.log('error', xhr, xhr.responseJSON);
                },
                complete: function () {
                    busy = false;
                }
            });
        });
    });
});
