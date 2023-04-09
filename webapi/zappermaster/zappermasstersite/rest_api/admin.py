from django.contrib import admin

from .models import Question, Remote, Button

admin.site.register(Question)
admin.site.register(Remote)
admin.site.register(Button)
