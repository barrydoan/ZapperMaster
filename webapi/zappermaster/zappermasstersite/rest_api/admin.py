from django.contrib import admin

from .models import Question, Remote, Button, Choice, Type, Manufacture

admin.site.register(Question)
admin.site.register(Choice)
admin.site.register(Remote)
admin.site.register(Button)
admin.site.register(Type)
admin.site.register(Manufacture)
