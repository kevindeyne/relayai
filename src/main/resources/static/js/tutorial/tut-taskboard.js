$(document).ready(function() {
    var firstTime = false;
    if(firstTime){
        let tour = new Shepherd.Tour({
          defaults: {
            classes: 'shepherd-theme-arrows',
            scrollTo: false,
            showCancelLink: true
          }
        });

         tour.addStep('intro', {
              title: 'Tutorial: Taskboard (1/3)',
              text: ['As this is the first time you have used the taskboard, we will quickly step through the basics of the screen.'],
              attachTo: '#taskurl right',
              buttons: [ {
                  text: 'Exit',
                  classes: 'shepherd-button-secondary',
                  action: tour.cancel
                }, {
                  text: 'Next',
                  action: tour.next
                }
              ]
         });

         tour.addStep('issuelist', {
              title: 'Tutorial: Taskboard (2/3)',
              text: ['Your browseable issues for the current sprint are shown in this area.', 'This is a dynamic list, updating live. The list is also dynamically sorted based on the importance and urgency of an issue.', 'The active issue is shown in blue.'],
              attachTo: 'section.active right',
              buttons: [
                {
                  text: 'Back',
                  classes: 'shepherd-button-secondary',
                  action: tour.back
                }, {
                  text: 'Next',
                  action: tour.next
                }
              ]
         });

         tour.addStep('issuelist', {
              title: 'Tutorial: Taskboard (3/3)',
              text: ['Your issues can be annoted with a colored circle. These shown the special state an issue is in: ',
              '- Red: A critical or priority one issue. These will always appear first.',
              '- Blue: An issue currently in progress.',
              '- Gray: A new issue that requires a workload assesment. Once classified, it will likely be planned into an upcoming sprint.',
              '- Orange: An issue from the previous sprint that did not get completed within the planned timeslot.',
              ],
              attachTo: 'section.active bottom',
              buttons: [
                {
                  text: 'Back',
                  classes: 'shepherd-button-secondary',
                  action: tour.back
                 }, {
                  text: 'Exit',
                  action: tour.cancel
                }
              ]
         });

        tour.start();
    }
});