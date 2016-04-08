module.exports = function (grunt) {
    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),
        concat: {
            css: {
                src: [
                    'css/**/*'
                ],
                dest: 'combined.css'
            },
            js: {
                src: [
                    'js/**/*'
                ],
                dest: 'combined.js'
            }
        },
        watch: {
            files: ['css/**/*', 'js/**/*'], tasks: ['concat']
        }
    });
    grunt.loadNpmTasks('grunt-contrib-concat');
    grunt.loadNpmTasks('grunt-contrib-watch');
    grunt.registerTask('default', ['concat:css', 'concat:js']);
};