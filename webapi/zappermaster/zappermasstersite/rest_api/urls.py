from django.urls import include, path
from rest_framework import routers
from . import views

router = routers.DefaultRouter()
router.register(r'users', views.UserViewSet)
router.register(r'groups', views.GroupViewSet)
router.register(r'questions', views.QuestionViewSet)
router.register(r'choices', views.ChoiceViewSet)
router.register(r'remotes', views.RemoteViewSet)
router.register(r'buttons', views.ButtonViewSet)
router.register(r'types', views.TypeViewSet)
router.register(r'manufacture', views.ManufactureViewSet)

urlpatterns = [
    path('', include(router.urls)),
]

