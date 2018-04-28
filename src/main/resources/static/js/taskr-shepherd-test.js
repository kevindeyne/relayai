$(document).ready(function() {
	let tour = new Shepherd.Tour({
      defaults: {
        classes: 'shepherd-theme-arrows',
        scrollTo: true,
        showCancelLink: true
      }
    });

    tour.addStep('example', {
      title: 'Example Shepherd',
      text: 'Creating a Shepherd is easy too! Just create ...',
      attachTo: '#taskurl right',
      advanceOn: '.docs-link click'
    });

    //tour.start();
});